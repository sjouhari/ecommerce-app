import { Component, input } from '@angular/core';
import { ProductCardComponent } from '../product-card/product-card.component';
import { Product } from '../../../models/product/product.model';

@Component({
    selector: 'app-product-list',
    imports: [ProductCardComponent],
    templateUrl: './product-list.component.html'
})
export class ProductListComponent {
    title = input<string>();
    products = input.required<Product[]>();
}
