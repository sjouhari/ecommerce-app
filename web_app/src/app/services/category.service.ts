import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category } from '../models/category/category.model';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/categories';

    getCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(this.baseUrl);
    }

    createCategory(category: Category): Observable<Category> {
        return this.http.post<Category>(this.baseUrl, category);
    }

    updateCategory(category: Category): Observable<Category> {
        return this.http.put<Category>(`${this.baseUrl}/${category.id}`, category);
    }

    deleteCategory(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
