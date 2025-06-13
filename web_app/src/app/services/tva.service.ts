import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TVA } from '../models/product/tva.model';

@Injectable({
    providedIn: 'root'
})
export class TvaService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/tvas';

    getAllTvas(): Observable<TVA[]> {
        return this.http.get<TVA[]>(this.baseUrl);
    }

    getTva(id: number): Observable<TVA> {
        return this.http.get<TVA>(`${this.baseUrl}/${id}`);
    }

    createTva(tva: TVA): Observable<TVA> {
        return this.http.post<TVA>(this.baseUrl, tva);
    }

    updateTva(tva: TVA): Observable<TVA> {
        return this.http.put<TVA>(`${this.baseUrl}/${tva.id}`, tva);
    }

    deleteTva(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
