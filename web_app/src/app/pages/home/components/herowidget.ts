import { Carousel } from 'primeng/carousel';
import { Component, model } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { GalleriaModule } from 'primeng/galleria';
import { RippleModule } from 'primeng/ripple';
import { PhotoService } from '../../service/photo.service';
import { CategoriesWidget } from './categorieswidget';
import { NewProductsWidget } from './newproductswidget';

@Component({
    selector: 'hero-widget',
    imports: [ButtonModule, RippleModule, GalleriaModule, Carousel, CategoriesWidget, NewProductsWidget],
    providers: [PhotoService],
    template: `
        <div id="hero" class="flex flex-col overflow-hidden">
            <p-carousel [value]="images()" [numVisible]="1" [numScroll]="1" [circular]="true" [autoplayInterval]="3000" [responsiveOptions]="responsiveOptions">
                <ng-template let-image #item>
                    <div class="border border-surface-200 dark:border-surface-700 rounded m-2">
                        <div class="">
                            <div class="relative mx-auto">
                                <img [src]="'assets/home/' + image" [alt]="image" class="w-full h-30rem rounded-border" />
                            </div>
                        </div>
                    </div>
                </ng-template>
            </p-carousel>
        </div>
        <categories-widget />
        <new-products-widget />
        <new-products-widget />
        <new-products-widget />
    `
})
export class HeroWidget {
    images = model([]);

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

    constructor(private photoService: PhotoService) {}

    ngOnInit() {
        this.images.set(['slide1.jpg', 'slide2.jpg', 'slide3.jpg'] as never[]);
    }
}
