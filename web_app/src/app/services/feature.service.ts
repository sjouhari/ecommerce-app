import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Feature } from '../models/user/feature.model';

@Injectable({
    providedIn: 'root'
})
export class FeatureService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/features';

    getFeatures(): Observable<Feature[]> {
        return this.http.get<Feature[]>(this.baseUrl);
    }

    createFeature(feature: Feature): Observable<Feature> {
        return this.http.post<Feature>(this.baseUrl, feature);
    }

    updateFeature(feature: Feature): Observable<Feature> {
        return this.http.put<Feature>(`${this.baseUrl}/${feature.id}`, feature);
    }

    deleteFeature(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
