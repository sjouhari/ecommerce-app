import { FileUpload } from 'primeng/fileupload';
import { SubCategory } from './../../models/category/sub-category.model';
import { Category } from './../../models/category/category.model';
import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
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
import { ProductColor } from '../../models/product/product-color';
import { AuthService } from '../../services/auth.service';
import { TVA } from '../../models/product/tva.model';
import { Media } from '../../models/product/media.model';
import { FloatLabelModule } from 'primeng/floatlabel';
import { IftaLabelModule } from 'primeng/iftalabel';
import { TvaService } from '../../services/tva.service';

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
        FileUpload,
        FloatLabelModule,
        IftaLabelModule
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
    tvaService = inject(TvaService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    productFormGroup!: FormGroup;

    products = signal<Product[]>([]);
    loading = signal<boolean>(false);
    statuses!: any[];
    categories = signal<Category[]>([]);
    subCategories = signal<SubCategory[]>([]);
    sizes: Size[] = [];
    tvas = signal<TVA[]>([]);
    colors = Object.entries(ProductColor).map(([key, value]) => ({ key, value }));

    selectedProducts!: Product[] | null;
    selectedCategory!: Category | null;
    selectedSubCategory!: SubCategory | null;
    uploadedFiles: any[] = [];
    productImages = signal<Media[]>([]);
    deletedProductImages = signal<string[]>([]);

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.statuses = [
            { label: 'Nouveau', value: 'NOUVEAU' },
            { label: 'Occasion', value: 'OCCASION' },
            { label: 'Remis à neuf', value: 'REMIS_A_NEUF' }
        ];

        this.initProductFormGroup();

        this.getProducts();

        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });

        this.tvaService.getAllTvas().subscribe({
            next: (tvas) => {
                this.tvas.set(tvas);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getProducts() {
        if (this.authService.isAdmin()) {
            this.productService.getProducts().subscribe({
                next: (products) => {
                    this.products.set(products);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.productService.getProductsByStoreId(this.authService.currentUser()?.store?.id!).subscribe({
                next: (products) => {
                    this.products.set(products);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }

    initProductFormGroup(product?: Product) {
        if (product && product.categoryName) {
            this.selectedCategory = this.categories().find((category) => category.name === product?.categoryName)!;
            this.subCategories.set(this.selectedCategory.subCategories);
            this.sizes = this.selectedCategory.sizes;
            this.productImages.set(product.medias);
        } else {
            this.selectedCategory = null;
            this.subCategories.set([]);
            this.sizes = [];
            this.productImages.set([]);
        }

        this.productFormGroup = this.formBuilder.group({
            id: new FormControl(product?.id || null),
            storeId: new FormControl(product?.store?.id || this.authService.currentUser()?.store?.id || null),
            tvaId: new FormControl(product?.tva?.id ?? null, [Validators.required]),
            name: new FormControl(product?.name || '', [Validators.required]),
            description: new FormControl(product?.description || '', [Validators.required]),
            status: new FormControl(product?.status || '', [Validators.required]),
            categoryId: new FormControl(this.selectedCategory?.id || '', [Validators.required]),
            subCategoryId: new FormControl(product?.subCategoryId || '', [Validators.required]),
            stock: new FormArray(
                (product?.stock || []).map((stock) =>
                    this.formBuilder.group({
                        id: new FormControl(stock.id || null),
                        productId: new FormControl(stock.productId || null),
                        size: new FormControl(stock.size, [Validators.required]),
                        color: new FormControl(stock.color, [Validators.required]),
                        quantity: new FormControl(stock.quantity, [Validators.required]),
                        price: new FormControl(stock.price, [Validators.required])
                    })
                )
            )
        });
    }

    get formControls() {
        return this.productFormGroup.controls;
    }

    onSelectCategory(categoryId: number) {
        this.selectedCategory = this.categories().find((category) => category.id === categoryId)!;
        this.subCategories.set(this.selectedCategory.subCategories);
        this.sizes = this.selectedCategory.sizes;
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

    async editProduct(product: Product) {
        this.initProductFormGroup(product);
        this.productDialog = true;
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
        if (this.productFormGroup.invalid) {
            this.productFormGroup.markAllAsTouched();
            return;
        }

        this.loading.set(true);

        const formData = new FormData();
        formData.append('product', JSON.stringify(this.productFormGroup.value));

        const productId = this.productFormGroup.value.id;
        if (productId) {
            this.uploadedFiles.forEach((image: File) => formData.append('newImages', image));
            formData.append('deletedImages', this.deletedProductImages().toString());
            this.productService.updateProduct(productId, formData).subscribe({
                next: (product) => {
                    this.getProducts();
                    this.productDialog = false;
                    this.productFormGroup.reset();
                    this.deletedProductImages.set([]);
                    this.uploadedFiles = [];
                    this.productImages.set([]);
                    this.loading.set(false);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'Le produit a été modifié avec succès.',
                        life: 3000
                    });
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                    this.loading.set(false);
                }
            });
        } else {
            if (this.uploadedFiles.length === 0) {
                this.loading.set(false);
                this.messageService.add({
                    severity: 'error',
                    summary: 'Erreur',
                    detail: 'Le produit doit avoir au moins une image.',
                    life: 3000
                });
                return;
            }
            this.uploadedFiles.forEach((image: File) => formData.append('images', image));
            this.productService.createProduct(formData).subscribe({
                next: (product) => {
                    this.getProducts();
                    this.productDialog = false;
                    this.productFormGroup.reset();
                    this.uploadedFiles = [];
                    this.loading.set(false);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Le produit a été créé avec succès.',
                        life: 3000
                    });
                },
                error: (error) => {
                    this.loading.set(false);
                    console.log(error); //TODO: handle error
                }
            });
        }
    }

    approveProduct(productId: number) {
        this.productService.approveProduct(productId).subscribe({
            next: () => {
                this.getProducts();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Approbation',
                    detail: 'Le produit a été approuvé avec succès.',
                    life: 3000
                });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    rejectProduct(productId: number) {
        this.productService.rejectProduct(productId).subscribe({
            next: () => {
                this.getProducts();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Rejet',
                    detail: 'Le produit a été rejeté avec succès.',
                    life: 3000
                });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    removeProductImage(imageUrl: string) {
        this.productImages.update((images) => images.filter((image) => image.url !== imageUrl));
        this.deletedProductImages.update((images) => [...images, imageUrl]);
    }

    onUpload(event: any) {
        console.log(this.uploadedFiles);
        for (const file of event.currentFiles) {
            this.uploadedFiles.push(file);
        }
    }
}
