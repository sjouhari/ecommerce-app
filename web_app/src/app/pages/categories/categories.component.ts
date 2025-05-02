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
    templateUrl: 'categories.component.html',
    providers: [MessageService, ConfirmationService]
})
export class CategoriesComponent implements OnInit {
    categoryDialog: boolean = false;

    categoryFormGroup!: FormGroup;

    categoryService = inject(CategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    categories = signal<Category[]>([]);

    selectedCategories!: Category[] | null;

    submitted: boolean = false;

    statuses!: any[];

    @ViewChild('dt') dt!: Table;

    exportColumns!: ExportColumn[];

    cols!: Column[];

    ngOnInit() {
        this.init();
    }

    init() {
        this.initCategoryFormGroup();
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            }
        });
    }

    initCategoryFormGroup(category?: Category) {
        this.categoryFormGroup = this.formBuilder.group({
            id: new FormControl(category?.id || null),
            name: new FormControl(category?.name || '', [Validators.required]),
            description: new FormControl(category?.description || '', [Validators.required])
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.initCategoryFormGroup();
        this.submitted = false;
        this.categoryDialog = true;
    }

    editCategory(category: Category) {
        this.initCategoryFormGroup(category);
        this.categoryDialog = true;
    }

    deleteSelectedCategories() {
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

    deleteCategory(category: Category) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la catégorie ' + category.name + ' ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.categoryService.deleteCategory(category!.id!).subscribe({
                    next: () => {
                        this.categories.update((categories) => categories.filter((val) => val.id !== category!.id));
                    }
                });
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Category Deleted',
                    life: 3000
                });
            }
        });
    }

    saveCategory() {
        if (this.categoryFormGroup.invalid) {
            return;
        }

        if (this.categoryFormGroup.value.id === null) {
            this.categoryService.createCategory(this.categoryFormGroup.value).subscribe({
                next: (category) => {
                    this.categories.update((categories) => [...categories, category]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Product Created',
                        life: 3000
                    });
                    this.categoryDialog = false;
                    this.categoryFormGroup.reset();
                }
            });
        } else {
            this.categoryService.updateCategory(this.categoryFormGroup.value).subscribe({
                next: (category) => {
                    this.init();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Product Created',
                        life: 3000
                    });
                    this.categoryDialog = false;
                    this.categoryFormGroup.reset();
                }
            });
        }
    }
}
