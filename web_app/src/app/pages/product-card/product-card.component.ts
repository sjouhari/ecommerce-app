import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Product } from '../../models/product/product.model';
import { CommonModule } from '@angular/common';
import { Tag } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-product-card',
    imports: [RouterLink, CommonModule, Tag, ButtonModule],
    templateUrl: './product-card.component.html'
})
export class ProductCardComponent {
    product = input.required<Product>();

    getPrice() {
        return this.product().stock[0].price + ' DH';
    }

    getQuantity() {
        return this.product().stock[0].quantity + ' pièces';
    }

    getSeverity(status: string) {
        switch (status) {
            case 'NOUVEAU':
                return 'success';
            case 'OCCASION':
                return 'warn';
            case 'REMIS_A_NEUF':
                return 'danger';
            default:
                return 'success';
        }
    }
}
