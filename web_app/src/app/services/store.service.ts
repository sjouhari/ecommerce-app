import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '../models/user/store.model';

@Injectable({
    providedIn: 'root'
})
export class StoreService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/stores';

    getStore(id: number): Observable<Store> {
        return this.http.get<Store>(`${this.baseUrl}/${id}`);
    }

    createStore(store: Store): Observable<Store> {
        return this.http.post<Store>(this.baseUrl, store);
    }

    updateStore(store: Store): Observable<Store> {
        return this.http.put<Store>(`${this.baseUrl}/${store.id}`, store);
    }

    deleteStore(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
