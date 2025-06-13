import { Component, model } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Carousel } from 'primeng/carousel';
import { RippleModule } from 'primeng/ripple';
import { NewProductsComponent } from '../new-products/new-products.component';
import { ListCategoriesComponent } from '../list-categories/list-categories.component';

@Component({
    selector: 'app-home',
    imports: [ButtonModule, RippleModule, Carousel, ListCategoriesComponent, NewProductsComponent],
    standalone: true,
    templateUrl: './home.component.html'
})
export class HomeComponent {
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

    ngOnInit() {
        this.images.set(['slide1.jpg', 'slide2.jpg', 'slide3.jpg'] as never[]);
    }
}
