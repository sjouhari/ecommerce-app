import { $t } from '@primeng/themes';
import { SubCategory } from './../../models/category/sub-category.model';
import { Category } from './../../models/category/category.model';
import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
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
import { ImageUploadComponent } from '../../components/image-upload/image-upload.component';
import { ProductColor } from '../../models/product/product-color';
import { AuthService } from '../../services/auth.service';
import { TVA } from '../../models/product/tva.model';

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
        ConfirmDialogModule,
        ImageUploadComponent
    ],
    templateUrl: 'products.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ProductsComponent implements OnInit {
    productDialog: boolean = false;
    tva: TVA = {
        id: 1,
        name: 'TVA 3',
        value: 0.05
    };

    productService = inject(ProductService);
    categoryService = inject(CategoryService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);

    formBuilder = inject(FormBuilder);
    productFormGroup!: FormGroup;

    products = signal<Product[]>([]);

    statuses!: any[];
    categories = signal<Category[]>([]);
    subCategories = signal<SubCategory[]>([]);
    sizes = signal<Size[]>([]);
    colors = Object.entries(ProductColor).map(([key, value]) => ({ key, value }));

    selectedProducts!: Product[] | null;
    selectedCategory!: Category | null;
    selectedSubCategory!: SubCategory | null;
    selectedImages = signal<File[]>([]);

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
            sellerId: new FormControl(product?.sellerId || this.authService.getCurrentUser()?.id),
            tva: new FormControl(this.tva),
            name: new FormControl(product?.name || '', [Validators.required]),
            description: new FormControl(product?.description || '', [Validators.required]),
            status: new FormControl(product?.status || '', [Validators.required]),
            categoryId: new FormControl(this.selectedCategory?.id || '', [Validators.required]),
            subCategoryId: new FormControl(product?.subCategoryId || '', [Validators.required]),
            stock: new FormArray(
                (product?.stock || []).map((stock) =>
                    this.formBuilder.group({
                        id: new FormControl(stock.id || null),
                        size: new FormControl(stock.size || null, [Validators.required]),
                        color: new FormControl(stock.color || '', [Validators.required]),
                        quantity: new FormControl(stock.quantity || '', [Validators.required]),
                        price: new FormControl(stock.price || '', [Validators.required])
                    })
                )
            )
        });
    }

    get formControls() {
        return this.productFormGroup.controls;
    }

    onSelectCategory(category: Category) {
        this.selectedCategory = category;
        this.subCategories.set(category.subCategories);
        this.sizes.set(category.sizes);
        this.addStockEntry();
    }

    get sizesFormArray(): FormArray {
        return this.productFormGroup.get('sizes') as FormArray;
    }

    get stockFormArray(): FormArray {
        return this.productFormGroup.get('stock') as FormArray;
    }

    addStockEntry(size?: Size) {
        this.stockFormArray.push(
            this.formBuilder.group({
                size: new FormControl(size?.libelle || null),
                color: new FormControl('', Validators.required),
                quantity: new FormControl('', Validators.required),
                price: new FormControl('', Validators.required)
            })
        );
    }

    removeStockEntry(index: number) {
        this.stockFormArray.removeAt(index);
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.productDialog = true;
        this.initProductFormGroup();
    }

    onImageUpload(event: any) {
        console.log(event.files);
        this.selectedImages.set(event.files);
    }

    editProduct(product: Product) {
        this.productDialog = true;
        this.initProductFormGroup(product);
    }

    deleteSelectedProducts() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer les produits sélectionnés ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedProducts?.forEach((product) => {
                    this.onDeleteProduct(product);
                });
                this.selectedProducts = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Les produits selectionnés ont été supprimés avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.productDialog = false;
        this.productFormGroup.reset();
    }

    deleteProduct(product: Product) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer le produit ' + product.name + ' ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.onDeleteProduct(product);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Le produit a été supprimé avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteProduct(product: Product) {
        this.productService.deleteProduct(product.id!).subscribe({
            next: () => {
                this.products.set(this.products().filter((val) => val.id !== product.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
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
        delete this.productFormGroup.value.categoryId;
        console.log(this.productFormGroup.value);
        console.log(this.selectedImages());
        if (this.productFormGroup.invalid) {
            this.productFormGroup.markAllAsTouched();
            return;
        }

        const formData = new FormData();
        formData.append('product', JSON.stringify(this.productFormGroup.value));
        this.selectedImages().forEach((image) => formData.append('images', image));

        if (this.productFormGroup.value.id) {
            console.log('MODIFICATION');
        } else {
            this.productService.createProduct(formData).subscribe({
                next: (product) => {
                    this.products.set([...this.products(), product]);
                    this.productDialog = false;
                    this.productFormGroup.reset();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Le produit a été créé avec succès.',
                        life: 3000
                    });
                }
            });
        }
    }
}
