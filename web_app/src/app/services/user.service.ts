import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user/user.model';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/users';

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.baseUrl);
    }

    getUser(id: number): Observable<User> {
        return this.http.get<User>(`${this.baseUrl}/${id}`);
    }

    createUser(user: User): Observable<User> {
        return this.http.post<User>(this.baseUrl, user);
    }

    updateUser(userId: number, user: User): Observable<User> {
        return this.http.put<User>(`${this.baseUrl}/${userId}`, user);
    }

    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    changePassword(id: number, currentPassword: string, newPassword: string): Observable<void> {
        return this.http.put<void>(`${this.baseUrl}/reset-password/${id}`, { currentPassword, newPassword });
    }
}
