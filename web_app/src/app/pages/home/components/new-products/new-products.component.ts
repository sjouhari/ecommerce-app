import { Component, inject } from '@angular/core';
import { Product } from '../../../../models/product/product.model';
import { ProductService } from '../../../../services/product.service';
import { ProductListComponent } from '../../../../user-layout/components/product-list/product-list.component';

@Component({
    selector: 'app-new-products',
    standalone: true,
    imports: [ProductListComponent],
    templateUrl: 'new-products.component.html'
})
export class NewProductsComponent {
    productService = inject(ProductService);
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
