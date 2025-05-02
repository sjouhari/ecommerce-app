import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Size } from '../models/category/size.model';

@Injectable({
    providedIn: 'root'
})
export class SizeService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/sizes';

    getSizes(): Observable<Size[]> {
        return this.http.get<Size[]>(this.baseUrl);
    }

    createSize(size: Size): Observable<Size> {
        return this.http.post<Size>(this.baseUrl, size);
    }

    updateSize(size: Size): Observable<Size> {
        return this.http.put<Size>(`${this.baseUrl}/${size.id}`, size);
    }

    deleteSize(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
