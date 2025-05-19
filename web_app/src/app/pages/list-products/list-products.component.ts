import { Component, signal, OnInit, inject } from '@angular/core';
import { Category } from '../../models/category/category.model';
import { CategoryService } from '../../services/category.service';
import { Checkbox } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { SubCategoryService } from '../../services/sub-category.service';
import { SizeService } from '../../services/size.service';
import { SubCategory } from '../../models/category/sub-category.model';
import { Size } from '../../models/category/size.model';
import { ProductColor } from '../../models/product/product-color';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product/product.model';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { ProductCardComponent } from '../product-card/product-card.component';
import { Slider } from 'primeng/slider';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';

@Component({
    selector: 'app-list-products',
    imports: [Checkbox, FormsModule, CommonModule, ButtonModule, ProductCardComponent, Slider, InputIconModule, IconFieldModule, InputTextModule],
    templateUrl: './list-products.component.html'
})
export class ListProductsComponent implements OnInit {
    productService = inject(ProductService);
    categoryService = inject(CategoryService);
    subCategoryService = inject(SubCategoryService);
    sizeService = inject(SizeService);

    products = signal<Product[]>([]);
    categories = signal<Category[]>([]);
    subCategories = signal<SubCategory[]>([]);
    sizes = signal<Size[]>([]);
    colors = Object.entries(ProductColor).map(([key, value]) => ({ key, value }));
    searchTerm = '';

    filteredProducts = signal<Product[]>([]);
    selectedCategories = signal<Category[]>([]);
    selectedSubCategories = signal<SubCategory[]>([]);
    selectedSizes = signal<Size[]>([]);
    selectedColors = signal<ProductColor[]>([]);
    selectedPrice = signal<number[]>([0, 100000]);

    ngOnInit(): void {
        this.productService.getProducts().subscribe({
            next: (products) => {
                this.products.set(products);
                this.filteredProducts.set(products);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    appliquerFilters() {
        this.filteredProducts.set(this.products());

        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            this.filteredProducts.update((products) => products.filter((product) => product.name.toLowerCase().includes(term) || product.description.toLowerCase().includes(term)));
        }
        console.log(this.filteredProducts());

        if (this.selectedCategories().length > 0) {
            this.subCategories.set(this.selectedCategories().flatMap((category) => category.subCategories));
            this.sizes.set(this.selectedCategories().flatMap((category) => category.sizes));
            this.filteredProducts.update((products) =>
                products.filter((product) => {
                    return this.selectedCategories().some((category) => product.categoryName === category.name);
                })
            );
        } else {
            this.subCategories.set([]);
            this.sizes.set([]);
            this.selectedSubCategories.set([]);
            this.selectedSizes.set([]);
        }

        if (this.selectedSubCategories().length > 0) {
            this.filteredProducts.update((products) =>
                products.filter((product) => {
                    return this.selectedSubCategories().some((subCategory) => product.subCategoryName === subCategory.name);
                })
            );
        }

        if (this.selectedSizes().length > 0) {
            this.filteredProducts.update((products) =>
                products.filter((product) => {
                    return this.selectedSizes().some((size) => product.stock.some((stock) => stock.size === size.libelle));
                })
            );
        }

        if (this.selectedColors().length > 0) {
            this.filteredProducts.update((products) =>
                products.filter((product) => {
                    return this.selectedColors().some((color) => product.stock.some((stock) => stock.color === color));
                })
            );
        }

        this.filteredProducts.update((products) =>
            products.filter((product) => {
                return product.stock.some((stock) => stock.price >= this.selectedPrice()[0] && stock.price <= this.selectedPrice()[1]);
            })
        );
    }
}
