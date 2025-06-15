import { Component, inject, model, signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Carousel } from 'primeng/carousel';
import { RippleModule } from 'primeng/ripple';
import { NewProductsComponent } from './components/new-products/new-products.component';
import { ListCategoriesComponent } from './components/list-categories/list-categories.component';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category/category.model';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product/product.model';
import { ProductListComponent } from '../../user-layout/components/product-list/product-list.component';

@Component({
    selector: 'app-home',
    imports: [ButtonModule, RippleModule, Carousel, ListCategoriesComponent, NewProductsComponent, ProductListComponent],
    standalone: true,
    templateUrl: './home.component.html'
})
export class HomeComponent {
    images = model([]);
    categoryService = inject(CategoryService);
    productService = inject(ProductService);

    categories = signal<Category[]>([]);
    productsByCategory: Map<number, Product[]> = new Map<number, Product[]>();

    responsiveOptions: any[] = [
        {
            breakpoint: '1300px',
            numVisible: 4
        },
        {
            breakpoint: '575px',
            numVisible: 1
        }
    ];

    ngOnInit() {
        this.images.set(['slide1.jpg', 'slide2.jpg', 'slide3.jpg'] as never[]);

        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });

        this.productService.getApprovedProducts().subscribe({
            next: (products) => {
                for (const category of this.categories()) {
                    this.productsByCategory.set(
                        category.id!,
                        products.filter((product) => product.categoryId === category.id)
                    );
                }
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
