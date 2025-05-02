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
    categoryDialog: boolean = false;

    subCategoryFormGroup!: FormGroup;

    subCategoryService = inject(SubCategoryService);
    categoryService = inject(CategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    subCategories = signal<SubCategory[]>([]);

    selectedCategories!: SubCategory[] | null;

    submitted: boolean = false;

    @ViewChild('dt') dt!: Table;

    exportColumns!: ExportColumn[];

    cols!: Column[];

    categories = signal<Category[]>([]);

    ngOnInit() {
        this.initSubCategoryFormGroup();
        this.subCategoryService.getSubCategories().subscribe({
            next: (subCategories) => {
                this.subCategories.set(subCategories);
            }
        });

        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            }
        });
    }

    initSubCategoryFormGroup(subCategory?: SubCategory) {
        this.subCategoryFormGroup = this.formBuilder.group({
            name: new FormControl(subCategory?.name || '', [Validators.required]),
            description: new FormControl(subCategory?.description || '', [Validators.required]),
            categoryId: new FormControl(subCategory?.categoryId || '', [Validators.required])
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.initSubCategoryFormGroup();
        this.submitted = false;
        this.categoryDialog = true;
    }

    editSubCategory(subCategory: SubCategory) {
        this.initSubCategoryFormGroup(subCategory);
        this.categoryDialog = true;
    }

    deleteSelectedSubCategories() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedCategories = null;
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
        this.categoryDialog = false;
        this.submitted = false;
    }

    deleteSubCategory(subCategory: SubCategory) {
        if (this.subCategoryFormGroup.invalid) {
            return;
        }
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la sous catégorie ' + subCategory.name + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.subCategoryService.deleteSubCategory(subCategory!.id!).subscribe({
                    next: () => {
                        this.subCategories.update((subCategories) => subCategories.filter((val) => val.id !== subCategory!.id));
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

    saveSubCategory() {
        if (this.subCategoryFormGroup.invalid) {
            return;
        }

        if (this.subCategoryFormGroup.value.id) {
            this.subCategoryService.updateSubCategory(this.subCategoryFormGroup.value).subscribe({
                next: () => {
                    this.subCategories.update((subCategories) =>
                        subCategories.map((subCategory) => {
                            if (subCategory.id === this.subCategoryFormGroup.value.id) {
                                return { ...subCategory, ...this.subCategoryFormGroup.value };
                            }
                            return subCategory;
                        })
                    );
                }
            });
        } else {
            this.subCategoryService.createSubCategory(this.subCategoryFormGroup.value).subscribe({
                next: () => {
                    this.subCategories.update((subCategories) => [...subCategories, this.subCategoryFormGroup.value]);
                }
            });
        }
    }
}
