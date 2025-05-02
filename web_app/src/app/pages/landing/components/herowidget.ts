import { Component, model } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { GalleriaModule } from 'primeng/galleria';
import { RippleModule } from 'primeng/ripple';
import { PhotoService } from '../../service/photo.service';

interface Image {
    itemImageSrc: string;
    thumbnailImageSrc: string;
    alt: string;
    title: string;
}

@Component({
    selector: 'hero-widget',
    imports: [ButtonModule, RippleModule, GalleriaModule],
    providers: [PhotoService],
    template: `
        <div
            id="hero"
            class="flex flex-col pt-6 px-6 lg:px-20 overflow-hidden"
            style="background: linear-gradient(0deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.2)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, rgb(238, 239, 175) 0%, rgb(195, 227, 250) 100%); clip-path: ellipse(150% 87% at 93% 13%)"
        >
            <div class="card p-0 bg-transparent border-0">
                <p-galleria [showThumbnails]="false" [value]="images()" [autoPlay]="true" [circular]="true" [responsiveOptions]="responsiveOptions" [showItemNavigators] [containerStyle]="{}">
                    <ng-template #item let-item>
                        <img [src]="item.itemImageSrc" style="width: 80%; height: 500px; display: block" />
                    </ng-template>
                </p-galleria>
            </div>
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
        this.photoService.getImages().then((images) => {
            this.photoService.getImages().then((images) => this.images.set(images as any));
        });
    }
}
