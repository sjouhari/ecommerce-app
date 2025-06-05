import { Component, signal, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { Checkbox } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { Slider } from 'primeng/slider';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { DropdownModule } from 'primeng/dropdown';

import { CategoryService } from '../../services/category.service';
import { SubCategoryService } from '../../services/sub-category.service';
import { SizeService } from '../../services/size.service';
import { ProductService } from '../../services/product.service';

import { Category } from '../../models/category/category.model';
import { SubCategory } from '../../models/category/sub-category.model';
import { Size } from '../../models/category/size.model';
import { ProductColor } from '../../models/product/product-color';
import { Product } from '../../models/product/product.model';

import { ProductCardComponent } from '../product-card/product-card.component';

@Component({
    selector: 'app-list-products',
    standalone: true,
    imports: [CommonModule, FormsModule, Checkbox, ButtonModule, Slider, IconFieldModule, InputIconModule, InputTextModule, DropdownModule, PaginatorModule, ProductCardComponent],
    templateUrl: './list-products.component.html'
})
export class ListProductsComponent implements OnInit {
    private productService = inject(ProductService);
    private categoryService = inject(CategoryService);
    private subCategoryService = inject(SubCategoryService);
    private sizeService = inject(SizeService);

    // Signals
    products = signal<Product[]>([]);
    filteredProducts = signal<Product[]>([]);
    categories = signal<Category[]>([]);
    subCategories = signal<SubCategory[]>([]);
    sizes = signal<Size[]>([]);
    colors = Object.entries(ProductColor).map(([key, value]) => ({ key, value }));

    // UI state
    searchTerm = '';
    selectedCategories = signal<Category[]>([]);
    selectedSubCategories = signal<SubCategory[]>([]);
    selectedSizes = signal<Size[]>([]);
    selectedColors = signal<string[]>([]);
    selectedPrice = signal<number[]>([0, 100000]);

    // Pagination
    first = 0;
    pageSize = 8;

    // Sorting
    sortOptions = [
        { label: 'Prix croissant', value: 'price-asc' },
        { label: 'Prix décroissant', value: 'price-desc' },
        { label: 'Nom A-Z', value: 'name-asc' },
        { label: 'Nom Z-A', value: 'name-desc' }
    ];
    selectedSort: string | null = null;

    ngOnInit(): void {
        this.productService.getProducts().subscribe({
            next: (products) => {
                this.products.set(products);
                this.filteredProducts.set(products);
            },
            error: (err) => console.error(err)
        });

        this.categoryService.getCategories().subscribe({
            next: (cats) => this.categories.set(cats),
            error: (err) => console.error(err)
        });
    }

    appliquerFilters(): void {
        let result = [...this.products()];

        // Search
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            result = result.filter((product) => product.name.toLowerCase().includes(term) || product.description.toLowerCase().includes(term));
        }

        // Categories
        if (this.selectedCategories().length > 0) {
            this.subCategories.set(this.selectedCategories().flatMap((cat) => cat.subCategories || []));
            this.sizes.set(this.selectedCategories().flatMap((cat) => cat.sizes || []));
            result = result.filter((product) => this.selectedCategories().some((cat) => product.categoryName === cat.name));
        } else {
            this.subCategories.set([]);
            this.sizes.set([]);
            this.selectedSubCategories.set([]);
            this.selectedSizes.set([]);
        }

        // Subcategories
        if (this.selectedSubCategories().length > 0) {
            result = result.filter((product) => this.selectedSubCategories().some((sub) => product.subCategoryName === sub.name));
        }

        // Sizes
        if (this.selectedSizes().length > 0) {
            result = result.filter((product) => this.selectedSizes().some((size) => product.stock?.some((s) => s.size === size.libelle)));
        }

        // Colors
        if (this.selectedColors().length > 0) {
            result = result.filter((product) => this.selectedColors().some((color) => product.stock?.some((s) => s.color === color)));
        }

        // Price Range
        const [min, max] = this.selectedPrice();
        result = result.filter((product) => product.stock?.some((s) => s.price >= min && s.price <= max));

        // Sorting
        switch (this.selectedSort) {
            case 'price-asc':
                result.sort((a, b) => this.minPrice(a) - this.minPrice(b));
                break;
            case 'price-desc':
                result.sort((a, b) => this.minPrice(b) - this.minPrice(a));
                break;
            case 'name-asc':
                result.sort((a, b) => a.name.localeCompare(b.name));
                break;
            case 'name-desc':
                result.sort((a, b) => b.name.localeCompare(a.name));
                break;
        }

        this.first = 0;
        this.filteredProducts.set(result);
    }

    toggleColor(colorKey: string) {
        const current = this.selectedColors();
        if (current.includes(colorKey)) {
            this.selectedColors.set(current.filter((c) => c !== colorKey));
        } else {
            this.selectedColors.set([...current, colorKey]);
        }
        this.appliquerFilters();
    }

    onPageChange(event: any): void {
        this.first = event.first;
        this.pageSize = event.rows;
    }

    resetFilters(): void {
        this.searchTerm = '';
        this.selectedCategories.set([]);
        this.selectedSubCategories.set([]);
        this.selectedSizes.set([]);
        this.selectedColors.set([]);
        this.selectedPrice.set([0, 100000]);
        this.selectedSort = null;
        this.appliquerFilters();
    }

    paginatedProducts(): Product[] {
        const start = this.first;
        const end = start + this.pageSize;
        return this.filteredProducts().slice(start, end);
    }

    selectedPriceLabel(): string {
        const [min, max] = this.selectedPrice();
        return `${min} MAD - ${max} MAD`;
    }

    private minPrice(product: Product): number {
        return Math.min(...(product.stock?.map((s) => s.price) || [Infinity]));
    }
}
