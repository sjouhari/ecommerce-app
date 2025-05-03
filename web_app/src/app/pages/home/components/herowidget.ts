import { Carousel } from 'primeng/carousel';
import { Component, model } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { GalleriaModule } from 'primeng/galleria';
import { RippleModule } from 'primeng/ripple';
import { PhotoService } from '../../service/photo.service';

@Component({
    selector: 'hero-widget',
    imports: [ButtonModule, RippleModule, GalleriaModule, Carousel],
    providers: [PhotoService],
    template: `
        <div id="hero" class="flex flex-col overflow-hidden">
            <p-carousel [value]="images()" [numVisible]="1" [numScroll]="1" [responsiveOptions]="responsiveOptions">
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
            <div class="mx-6 md:mx-20 mt-0 md:mt-6">
                <h1 class="text-6xl font-bold text-gray-900 leading-tight"><span class="font-light block">Eu sem integer</span>eget magna fermentum</h1>
                <p class="font-normal text-2xl leading-normal md:mt-4 text-gray-700">Sed blandit libero volutpat sed cras. Fames ac turpis egestas integer. Placerat in egestas erat...</p>
                <button pButton pRipple [rounded]="true" type="button" label="Get Started" class="!text-xl mt-8 !px-4"></button>
            </div>
            <div class="flex justify-center md:justify-end">
                <img src="https://primefaces.org/cdn/templates/sakai/landing/screen-1.png" alt="Hero Image" class="w-9/12 md:w-auto" />
            </div>
        </div>
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
