import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../models/order/order.model';
import { OrderRequest } from '../models/order/order-request.model';
import { BestSellingProduct } from '../models/order/best-selling-product.model';

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/orders';

    getOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(this.baseUrl);
    }

    getUserOrders(userId: number): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.baseUrl}/user/${userId}`);
    }

    getOrder(id: string): Observable<Order> {
        return this.http.get<Order>(`${this.baseUrl}/${id}`);
    }

    placeOrder(order: OrderRequest): Observable<Order> {
        return this.http.post<Order>(this.baseUrl, order);
    }

    deleteOrder(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    getBestSellingProducts(): Observable<BestSellingProduct[]> {
        return this.http.get<BestSellingProduct[]>(`${this.baseUrl}/stats/best-selling-products`);
    }
}
