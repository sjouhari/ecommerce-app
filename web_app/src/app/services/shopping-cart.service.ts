import { OrderItem } from './../models/order/order-item.model';
import { ShoppingCart } from './../models/order/shopping-cart.model';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class ShoppingCartService {
    http = inject(HttpClient);
    authService = inject(AuthService);

    shoppingCart = signal<ShoppingCart | null>(null);

    constructor() {
        if (this.authService.isLoggedIn()) {
            this.getShoppingCart(this.authService.currentUser()?.id!).subscribe((cart) => {
                this.setShoppingCart(cart);
            });
        }
    }

    setShoppingCart(cart: ShoppingCart) {
        this.shoppingCart.set(cart);
    }

    baseUrl = 'http://localhost:8080/api/shopping-cart';

    getShoppingCart(userId: number): Observable<ShoppingCart> {
        return this.http.get<ShoppingCart>(`${this.baseUrl}/${userId}`);
    }

    addItemToShoppingCart(userId: number, OrderItem: OrderItem): Observable<ShoppingCart> {
        return this.http.post<ShoppingCart>(`${this.baseUrl}/${userId}`, OrderItem);
    }

    updateItemQuantity(orderItemId: number, quantity: number): Observable<OrderItem> {
        return this.http.put<OrderItem>(`${this.baseUrl}/quantity/${orderItemId}`, { quantity });
    }

    selectItem(orderItemId: number, selected: boolean): Observable<OrderItem> {
        return this.http.put<OrderItem>(`${this.baseUrl}/select/${orderItemId}`, { selected });
    }

    deleteItemFromShoppingCart(orderItemId: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/${orderItemId}`);
    }
}
