import { SubCategory } from './../../models/category/sub-category.model';
import { Category } from './../../models/category/category.model';
import { Component, computed, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
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
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product/product.model';
import { CategoryService } from '../../services/category.service';
import { Size } from '../../models/category/size.model';
import { CheckboxModule } from 'primeng/checkbox';

@Component({
    selector: 'app-products',
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
        CheckboxModule,
        InputNumberModule,
        DialogModule,
        TagModule,
        InputIconModule,
        IconFieldModule,
        ConfirmDialogModule
    ],
    templateUrl: 'products.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ProductsComponent implements OnInit {
    productDialog: boolean = false;

    productService = inject(ProductService);
    categoryService = inject(CategoryService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);

    formBuilder = inject(FormBuilder);
    productFormGroup!: FormGroup;

    products = signal<Product[]>([]);

    statuses!: any[];
    categories = signal<Category[]>([]);
    subCategories = signal<SubCategory[]>([]);
    sizes = signal<Size[]>([]);

    selectedProducts!: Product[] | null;
    selectedCategory!: Category | null;
    selectedSubCategory!: SubCategory | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.statuses = [
            { label: 'Nouveau', value: 'NOUVEAU' },
            { label: 'Occasion', value: 'OCCASION' },
            { label: 'Remis à neuf', value: 'REMIS_A_NEUF' }
        ];

        this.initProductFormGroup();

        this.productService.getProducts().subscribe({
            next: (products) => {
                this.products.set(products);
            }
        });

        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            }
        });
    }

    initProductFormGroup(product?: Product) {
        this.productFormGroup = this.formBuilder.group({
            id: new FormControl(product?.id || null),
            name: new FormControl(product?.name || '', [Validators.required]),
            description: new FormControl(product?.description || '', [Validators.required]),
            status: new FormControl(product?.status || '', [Validators.required]),
            categoryId: new FormControl(this.selectedCategory?.id || '', [Validators.required]),
            subCategoryId: new FormControl(product?.subCategoryId || '', [Validators.required]),
            sizes: new FormControl(this.selectedSubCategory?.sizes, [Validators.required]),
            stock: new FormArray(
                (product?.stock || []).map((stock) =>
                    this.formBuilder.group({
                        id: new FormControl(stock.id || null),
                        size: new FormControl(stock.size || null),
                        color: new FormControl(stock.color || '', [Validators.required]),
                        quantity: new FormControl(stock.quantity || '', [Validators.required]),
                        price: new FormControl(stock.price || '', [Validators.required])
                    })
                )
            )
        });
    }

    onSelectCategory(category: Category) {
        this.selectedCategory = category;
        this.subCategories.set(category.subCategories);
    }

    onSelectSubCategory(subCategory: SubCategory) {
        this.selectedSubCategory = subCategory;
        this.sizes.set(subCategory.sizes);
    }

    get stock(): FormArray {
        return this.productFormGroup.get('stock') as FormArray;
    }

    addStockEntry() {
        this.stock.push(
            this.formBuilder.group({
                size: new FormControl(null),
                color: new FormControl('', Validators.required),
                quantity: new FormControl('', Validators.required),
                price: new FormControl('', Validators.required)
            })
        );
    }

    removeStockEntry(index: number) {
        this.stock.removeAt(index);
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.productDialog = true;
    }

    editProduct(product: Product) {
        this.productDialog = true;
        this.initProductFormGroup(product);
    }

    deleteSelectedProducts() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.products.set(this.products().filter((val) => !this.selectedProducts?.includes(val)));
                this.selectedProducts = null;
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
        this.productDialog = false;
    }

    deleteProduct(product: Product) {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete ' + product.name + '?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.products.set(this.products().filter((val) => val.id !== product.id));
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Deleted',
                    life: 3000
                });
            }
        });
    }

    findIndexById(id: string): number {
        let index = -1;
        for (let i = 0; i < this.products().length; i++) {
            if (this.products()[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    }

    getSeverity(status: string) {
        switch (status) {
            case 'NOUVEAU':
                return 'success';
            case 'REMIS_A_NEUF':
                return 'warn';
            case 'OCCASION':
                return 'danger';
            default:
                return 'info';
        }
    }

    saveProduct() {
        let _products = this.products();
        if (true) {
            if (true) {
                this.products.set([..._products]);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Updated',
                    life: 3000
                });
            } else {
            }

            this.productDialog = false;
        }
    }
}
