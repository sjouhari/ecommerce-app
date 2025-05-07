import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Carousel } from 'primeng/carousel';
import { Tag } from 'primeng/tag';
import { PhotoService } from '../../service/photo.service';
import { Product } from '../../../models/product/product.model';
import { ProductService } from '../../service/product.service';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'new-products-widget',
    standalone: true,
    imports: [CommonModule, Carousel, Tag, ButtonModule, RouterLink],
    providers: [PhotoService, ProductService],
    template: `
        <div id="new-products" class="p-6" style="background: linear-gradient(0deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.6)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, #efe1af 0%, #c3dcfa 100%)">
            <div class="grid grid-cols-12 gap-4 justify-center">
                <div class="col-span-12 text-center mb-3">
                    <div class="text-surface-900 dark:text-surface-0 font-normal mb-2 text-4xl">Produits</div>
                    <span class="text-muted-color text-2xl">Notre catalogue des produits</span>
                </div>

                <p-carousel class="col-span-12" [value]="products" [circular]="true" [autoplayInterval]="3000" [numVisible]="4" [numScroll]="3" [circular]="false" [responsiveOptions]="carouselResponsiveOptions">
                    <ng-template let-product #item>
                        <div class="border border-surface rounded-border m-2 p-4">
                            <div class="mb-4">
                                <div class="relative mx-auto">
                                    <img src="https://primefaces.org/cdn/primeng/images/demo/product/{{ product.image }}" [alt]="product.name" class="w-full rounded-border" />
                                    <div class="absolute bg-black/70 rounded-border" [ngStyle]="{ 'left.px': 5, 'top.px': 5 }">
                                        <p-tag [value]="product.inventoryStatus" [severity]="getSeverity(product.inventoryStatus)" />
                                    </div>
                                </div>
                            </div>
                            <div class="mb-4 font-medium">{{ product.name }}</div>
                            <div class="flex justify-between items-center">
                                <div class="mt-0 font-semibold text-xl">{{ '$' + product.price }}</div>
                                <span>
                                    <p-button icon="pi pi-heart" severity="secondary" [outlined]="true" />
                                    <p-button icon="pi pi-shopping-cart" styleClass="ml-2" />
                                </span>
                            </div>
                        </div>
                    </ng-template>
                </p-carousel>
            </div>
            <div class="col-span-12 text-center">
                <a class="text-blue-500 hover:text-blue-700" routerLink="/list-products">Voir tous les produits</a>
            </div>
        </div>
    `
})
export class NewProductsWidget {
    products!: Product[];

    images!: any[];

    galleriaResponsiveOptions: any[] = [
        {
            breakpoint: '1024px',
            numVisible: 5
        },
        {
            breakpoint: '960px',
            numVisible: 4
        },
        {
            breakpoint: '768px',
            numVisible: 3
        },
        {
            breakpoint: '560px',
            numVisible: 1
        }
    ];

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

    constructor(
        private productService: ProductService,
        private photoService: PhotoService
    ) {}

    ngOnInit() {
        this.productService.getProductsSmall().then((products) => {
            this.products = products;
        });

        this.photoService.getImages().then((images) => {
            this.images = images;
        });
    }

    getSeverity(status: string) {
        switch (status) {
            case 'INSTOCK':
                return 'success';
            case 'LOWSTOCK':
                return 'warn';
            case 'OUTOFSTOCK':
                return 'danger';
            default:
                return 'success';
        }
    }
}
