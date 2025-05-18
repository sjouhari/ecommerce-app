import { Component, inject, OnInit, signal } from '@angular/core';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { ShoppingCart } from '../../models/order/shopping-cart.model';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { InputNumber } from 'primeng/inputnumber';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-shopping-cart',
    imports: [CommonModule, InputNumber, FormsModule, ButtonModule],
    templateUrl: './shopping-cart.component.html'
})
export class ShoppingCartComponent implements OnInit {
    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);

    selectedQuantity: number = 1;

    shoppingCart = signal<ShoppingCart | null>(null);

    ngOnInit() {
        this.shoppingCartService.getShoppingCart(this.authService.getCurrentUser()?.id!).subscribe({
            next: (cart) => {
                this.shoppingCart.set(cart);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getProduct(productId: string): string {
        return `http://localhost:8080/api/products/images`;
    }

    updateItemQuantity() {}

    removeItem(id: number) {}

    calculateTotalPrice() {
        return this.shoppingCart()?.orderItems.reduce((acc, item) => acc + item.price * item.quantity, 0);
    }

    checkout() {}
}
