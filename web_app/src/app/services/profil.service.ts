import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Profil } from '../models/user/profil.model';

@Injectable({
    providedIn: 'root'
})
export class ProfilService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/profils';

    getProfils(): Observable<Profil[]> {
        return this.http.get<Profil[]>(this.baseUrl);
    }

    createProfil(profil: Profil): Observable<Profil> {
        return this.http.post<Profil>(this.baseUrl, profil);
    }

    updateProfil(profil: Profil): Observable<Profil> {
        return this.http.put<Profil>(`${this.baseUrl}/${profil.id}`, profil);
    }

    deleteProfil(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
