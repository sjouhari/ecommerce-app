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
import { Category } from '../../models/category/category.model';
import { SubCategoryService } from '../../services/sub-category.service';
import { SubCategory } from '../../models/category/sub-category.model';
import { CategoryService } from '../../services/category.service';

@Component({
    selector: 'app-categories',
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
    templateUrl: 'sub-categories.component.html',
    providers: [MessageService, ConfirmationService]
})
export class SubCategoriesComponent implements OnInit {
    subCategoryDialog: boolean = false;

    subCategoryFormGroup!: FormGroup;

    subCategoryService = inject(SubCategoryService);
    categoryService = inject(CategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    subCategories = signal<SubCategory[]>([]);

    selectedCategories!: SubCategory[] | null;

    @ViewChild('dt') dt!: Table;

    categories = signal<Category[]>([]);
    loading = signal(false);

    ngOnInit() {
        this.init();
    }

    init() {
        this.initSubCategoryFormGroup();
        this.subCategoryService.getSubCategories().subscribe({
            next: (subCategories) => {
                this.subCategories.set(subCategories);
            }
        });
    }

    get formControls() {
        return this.subCategoryFormGroup.controls;
    }

    initSubCategoryFormGroup(subCategory?: SubCategory) {
        this.subCategoryFormGroup = this.formBuilder.group({
            id: new FormControl(subCategory?.id || null),
            name: new FormControl(subCategory?.name || '', [Validators.required]),
            description: new FormControl(subCategory?.description || '', [Validators.required]),
            categoryId: new FormControl(subCategory?.categoryId || '', [Validators.required])
        });
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.categoryService.getCategories().subscribe({
            next: (allCategories) => {
                this.categories.set(allCategories);
                this.initSubCategoryFormGroup();
                if (allCategories.length > 0) {
                    this.subCategoryDialog = true;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: 'Vous devez créer au moins une catégorie avant de pouvoir créer une sous catégorie',
                        life: 3000
                    });
                }
            },
            error: (error) => {
                console.log(error); // TODO: handle error
            }
        });
    }

    editSubCategory(subCategory: SubCategory) {
        this.initSubCategoryFormGroup(subCategory);
        this.subCategoryDialog = true;
    }

    deleteSelectedSubCategories() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer les sous catégories sélectionnées ? Toutes les tailles seront supprimées. Cette action est irréversible.',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedCategories?.forEach((subCategory) => this.onDeleteSubCategory(subCategory));
                this.selectedCategories = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les sous catégories sélectionnées et leurs tailles ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.subCategoryDialog = false;
    }

    deleteSubCategory(subCategory: SubCategory) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la sous catégorie ' + subCategory.name + '? Toutes les tailles seront supprimées. Cette action est irréversible.',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.onDeleteSubCategory(subCategory);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'La sous catégorie a été supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteSubCategory(subCategory: SubCategory) {
        this.subCategoryService.deleteSubCategory(subCategory!.id!).subscribe({
            next: () => {
                this.subCategories.update((subCategories) => subCategories.filter((val) => val.id !== subCategory!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    saveSubCategory() {
        if (this.subCategoryFormGroup.invalid) {
            this.subCategoryFormGroup.markAllAsTouched();
            return;
        }

        this.loading.set(true);
        if (this.subCategoryFormGroup.value.id === null) {
            this.subCategoryService.createSubCategory(this.subCategoryFormGroup.value).subscribe({
                next: (subCategory) => {
                    this.subCategories.update((subCategories) => [...subCategories, subCategory]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'La sous catégorie a été créée avec succès.',
                        life: 3000
                    });
                    this.subCategoryDialog = false;
                    this.loading.set(false);
                    this.subCategoryFormGroup.reset();
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.subCategoryService.updateSubCategory(this.subCategoryFormGroup.value).subscribe({
                next: (category) => {
                    this.init();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'La sous catégorie a été modifiée avec succès.',
                        life: 3000
                    });
                    this.subCategoryDialog = false;
                    this.loading.set(false);
                    this.subCategoryFormGroup.reset();
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
