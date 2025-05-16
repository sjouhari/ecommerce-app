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

    getProduct(id: string): Observable<Product> {
        return this.http.get<Product>(`${this.baseUrl}/${id}`);
    }

    getNewProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(this.baseUrl + '/new');
    }

    createProduct(formData: FormData): Observable<Product> {
        return this.http.post<Product>(this.baseUrl, formData);
    }

    updateProduct(product: Product): Observable<Product> {
        return this.http.put<Product>(`${this.baseUrl}/${product.id}`, product);
    }

    deleteProduct(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
