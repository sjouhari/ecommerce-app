import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../models/order/order.model';

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/orders';

    getOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(this.baseUrl);
    }

    getOrder(id: string): Observable<Order> {
        return this.http.get<Order>(`${this.baseUrl}/${id}`);
    }

    placeOrder(order: Order): Observable<Order> {
        return this.http.post<Order>(this.baseUrl, order);
    }

    updateOrder(product: Order): Observable<Order> {
        return this.http.put<Order>(`${this.baseUrl}/${product.id}`, product);
    }

    deleteOrder(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
