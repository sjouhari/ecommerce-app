import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubCategory } from '../models/category/sub-category.model';

@Injectable({
    providedIn: 'root'
})
export class SubCategoryService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/subcategories';

    getSubCategories(): Observable<SubCategory[]> {
        return this.http.get<SubCategory[]>(this.baseUrl);
    }

    createSubCategory(category: SubCategory): Observable<SubCategory> {
        return this.http.post<SubCategory>(this.baseUrl, category);
    }

    updateSubCategory(category: SubCategory): Observable<SubCategory> {
        return this.http.put<SubCategory>(`${this.baseUrl}/${category.id}`, category);
    }

    deleteSubCategory(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
