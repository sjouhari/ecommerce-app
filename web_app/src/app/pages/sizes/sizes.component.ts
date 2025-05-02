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
import { SubCategoryService } from '../../services/sub-category.service';
import { SizeService } from '../../services/size.service';
import { SubCategory } from '../../models/category/sub-category.model';

interface Column {
    field: string;
    header: string;
    customExportHeader?: string;
}

interface ExportColumn {
    title: string;
    dataKey: string;
}

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
        ConfirmDialogModule
    ],
    templateUrl: 'sizes.component.html',
    providers: [MessageService, ConfirmationService]
})
export class SizesComponent implements OnInit {
    sizeDialog: boolean = false;

    sizeFormGroup!: FormGroup;

    sizeService = inject(SizeService);
    subCategoryService = inject(SubCategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    sizes = signal<Size[]>([]);

    selectedSizes!: Size[] | null;

    submitted: boolean = false;

    @ViewChild('dt') dt!: Table;

    exportColumns!: ExportColumn[];

    cols!: Column[];

    subCategories = signal<SubCategory[]>([]);

    ngOnInit() {
        this.initSizeFormGroup();
        this.sizeService.getSizes().subscribe({
            next: (sizes) => {
                this.sizes.set(sizes);
            }
        });

        this.subCategoryService.getSubCategories().subscribe({
            next: (subCategories) => {
                this.subCategories.set(subCategories);
            }
        });
    }

    initSizeFormGroup(size?: Size) {
        this.sizeFormGroup = this.formBuilder.group({
            id: new FormControl(size?.id || null),
            libelle: new FormControl(size?.libelle || '', [Validators.required]),
            subCategoryId: new FormControl(size?.subCategoryId || '', [Validators.required])
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.initSizeFormGroup();
        this.submitted = false;
        this.sizeDialog = true;
    }

    editSize(size: Size) {
        this.initSizeFormGroup(size);
        this.sizeDialog = true;
    }

    deleteSelectedSizes() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedSizes = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Products Deleted',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.sizeDialog = false;
        this.submitted = false;
    }

    deleteSize(size: Size) {
        if (this.sizeFormGroup.invalid) {
            return;
        }
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la sous catégorie ' + size.libelle + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.sizeService.deleteSize(size!.id!).subscribe({
                    next: () => {
                        this.sizes.update((subCategories) => subCategories.filter((val) => val.id !== size!.id));
                    }
                });
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'SubCategory Deleted',
                    life: 3000
                });
            }
        });
    }

    saveSize() {
        if (this.sizeFormGroup.invalid) {
            return;
        }

        if (this.sizeFormGroup.value.id) {
            this.sizeService.updateSize(this.sizeFormGroup.value).subscribe({
                next: () => {
                    this.sizes.update((subCategories) =>
                        subCategories.map((subCategory) => {
                            if (subCategory.id === this.sizeFormGroup.value.id) {
                                return { ...subCategory, ...this.sizeFormGroup.value };
                            }
                            return subCategory;
                        })
                    );
                }
            });
        } else {
            this.sizeService.createSize(this.sizeFormGroup.value).subscribe({
                next: () => {
                    this.sizes.update((subCategories) => [...subCategories, this.sizeFormGroup.value]);
                }
            });
        }
    }
}
