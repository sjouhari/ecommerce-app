import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product/product.model';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/products';

    getProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(this.baseUrl);
    }

    getApprovedProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(`${this.baseUrl}/approved`);
    }

    getProductsByStoreId(storeId: number): Observable<Product[]> {
        return this.http.get<Product[]>(`${this.baseUrl}/store/${storeId}`);
    }

    getProductsByCategory(categoryId: number): Observable<Product[]> {
        return this.http.get<Product[]>(`${this.baseUrl}/category/${categoryId}`);
    }

    getProduct(id: string): Observable<Product> {
        return this.http.get<Product>(`${this.baseUrl}/${id}`);
    }

    getNewProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(this.baseUrl + '/new');
    }

    createProduct(formData: FormData): Observable<Product> {
        return this.http.post<Product>(this.baseUrl, formData);
    }

    updateProduct(id: number, formData: FormData): Observable<Product> {
        return this.http.put<Product>(`${this.baseUrl}/${id}`, formData);
    }

    deleteProduct(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    approveProduct(id: number): Observable<void> {
        return this.http.put<void>(`${this.baseUrl}/approve/${id}`, {});
    }

    rejectProduct(id: number): Observable<void> {
        return this.http.put<void>(`${this.baseUrl}/reject/${id}`, {});
    }
}
