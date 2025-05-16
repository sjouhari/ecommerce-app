import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Carousel } from 'primeng/carousel';
import { Tag } from 'primeng/tag';
import { PhotoService } from '../../service/photo.service';
import { Product } from '../../../models/product/product.model';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../services/product.service';
import { ProductCardComponent } from '../../product-card/product-card.component';

@Component({
    selector: 'new-products-widget',
    standalone: true,
    imports: [Carousel, RouterLink, ProductCardComponent],
    providers: [PhotoService],
    template: `
        <div id="new-products" class="p-6">
            <div class="grid grid-cols-12 gap-4 justify-center">
                <div class="col-span-12 text-center mb-3">
                    <div class="text-surface-900 dark:text-surface-0 font-normal mb-2 text-4xl">Produits</div>
                    <span class="text-muted-color text-2xl">Notre catalogue des produits</span>
                </div>

                <p-carousel class="col-span-12" [value]="products" [circular]="true" [autoplayInterval]="3000" [numVisible]="4" [numScroll]="3" [circular]="false" [responsiveOptions]="carouselResponsiveOptions">
                    <ng-template let-product #item>
                        <app-product-card [product]="product"></app-product-card>
                    </ng-template>
                </p-carousel>
            </div>
            <div class="col-span-12 text-center">
                <a class="text-blue-500 hover:text-blue-700" routerLink="/home/list-products">Voir tous les produits</a>
            </div>
        </div>
    `
})
export class NewProductsWidget {
    products!: Product[];

    carouselResponsiveOptions: any[] = [
        {
            breakpoint: '1024px',
            numVisible: 3,
            numScroll: 3
        },
        {
            breakpoint: '768px',
            numVisible: 2,
            numScroll: 2
        },
        {
            breakpoint: '560px',
            numVisible: 1,
            numScroll: 1
        }
    ];

    constructor(private productService: ProductService) {}

    ngOnInit() {
        this.productService.getNewProducts().subscribe({
            next: (products) => {
                this.products = products;
            },
            error: (error) => {
                console.log(error); // TODO: handle error
            }
        });
    }
}
