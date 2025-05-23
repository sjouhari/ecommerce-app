import { Size } from './../../models/category/size.model';
import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SizeService } from '../../services/size.service';
import { Category } from '../../models/category/category.model';
import { CategoryService } from '../../services/category.service';
import { FloatLabelModule } from 'primeng/floatlabel';

@Component({
    selector: 'app-sizes',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        ReactiveFormsModule,
        ButtonModule,
        RippleModule,
        ToastModule,
        ToolbarModule,
        RatingModule,
        InputTextModule,
        TextareaModule,
        SelectModule,
        RadioButtonModule,
        InputNumberModule,
        DialogModule,
        TagModule,
        InputIconModule,
        IconFieldModule,
        ConfirmDialogModule,
        FloatLabelModule
    ],
    templateUrl: 'sizes.component.html',
    providers: [MessageService, ConfirmationService]
})
export class SizesComponent implements OnInit {
    sizeDialog: boolean = false;

    sizeFormGroup!: FormGroup;

    sizeService = inject(SizeService);
    categoryService = inject(CategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    sizes = signal<Size[]>([]);
    loading = signal(false);

    selectedSizes!: Size[] | null;

    @ViewChild('dt') dt!: Table;

    categories = signal<Category[]>([]);

    ngOnInit() {
        this.init();
    }

    init() {
        this.initSizeFormGroup();
        this.sizeService.getSizes().subscribe({
            next: (sizes) => {
                this.sizes.set(sizes);
            }
        });
    }

    initSizeFormGroup(size?: Size) {
        this.sizeFormGroup = this.formBuilder.group({
            id: new FormControl(size?.id || null),
            libelle: new FormControl(size?.libelle || '', [Validators.required]),
            categoryId: new FormControl(size?.categoryId || '', [Validators.required])
        });
    }

    get formControls() {
        return this.sizeFormGroup.controls;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                if (categories.length > 0) {
                    this.categories.set(categories);
                    this.initSizeFormGroup();
                    this.sizeDialog = true;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Erreur',
                        detail: 'Vous devez créer au moins une catégorie avant de pouvoir créer une taille',
                        life: 3000
                    });
                }
            },
            error: (error) => {
                console.log(error); // TODO: handle error
            }
        });
    }

    editSize(size: Size) {
        this.initSizeFormGroup(size);
        this.sizeDialog = true;
    }

    deleteSelectedSizes() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer toutes les tailles sélectionnées ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedSizes?.forEach((size) => this.onDeleteSize(size));
                this.selectedSizes = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les tailles sélectionnées ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.sizeDialog = false;
        this.sizeFormGroup.reset();
    }

    deleteSize(size: Size) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la taille ' + size.libelle + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.onDeleteSize(size);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'La taille a été supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteSize(size: Size) {
        this.sizeService.deleteSize(size!.id!).subscribe({
            next: () => {
                this.sizes.update((subCategories) => subCategories.filter((val) => val.id !== size!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    saveSize() {
        if (this.sizeFormGroup.invalid) {
            this.sizeFormGroup.markAllAsTouched();
            return;
        }

        this.loading.set(true);
        if (this.sizeFormGroup.value.id === null) {
            this.sizeService.createSize(this.sizeFormGroup.value).subscribe({
                next: (size) => {
                    this.sizes.update((sizes) => [...sizes, size]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'La taille a été créée avec succès.',
                        life: 3000
                    });
                    this.sizeDialog = false;
                    this.loading.set(false);
                    this.sizeFormGroup.reset();
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.sizeService.updateSize(this.sizeFormGroup.value).subscribe({
                next: (size) => {
                    this.init();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'La taille a été modifiée avec succès.',
                        life: 3000
                    });
                    this.sizeDialog = false;
                    this.loading.set(false);
                    this.sizeFormGroup.reset();
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
