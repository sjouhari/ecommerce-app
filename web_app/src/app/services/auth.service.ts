import { User } from '../models/user/user.model';
import { ForgotPasswordRequest } from './../models/auth/forgot-password.model';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/auth/login.model';
import { RegisterRequest, RegisterResponse } from '../models/auth/register.model';
import { ForgotPasswordResponse } from '../models/auth/forgot-password.model';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private tokenKey = 'token';
    private baseUrl = 'http://localhost:8080/api';

    httpClient = inject(HttpClient);
    router = inject(Router);

    currentUser = signal<User | null>(null);

    loadAuthState(): Promise<void> {
        return new Promise<void>((resolve) => {
            if (this.isTokenExpired()) {
                this.removeToken();
                resolve();
            } else {
                this.getCurrentUser().subscribe({
                    next: (user) => {
                        this.setCurrentUser(user);
                        resolve();
                    },
                    error: () => {
                        this.removeToken();
                        resolve();
                    }
                });
            }
        });
    }

    login(loginRequest: LoginRequest): Observable<LoginResponse> {
        return this.httpClient.post<LoginResponse>(`${this.baseUrl}/auth/login`, loginRequest);
    }

    register(registerRequest: RegisterRequest): Observable<RegisterResponse> {
        return this.httpClient.post<RegisterResponse>(`${this.baseUrl}/auth/register`, registerRequest);
    }

    logout(): void {
        this.removeToken();
        this.router.navigate(['/login']);
    }

    forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Observable<ForgotPasswordResponse> {
        return this.httpClient.get<ForgotPasswordResponse>(`${this.baseUrl}/users/forgot-password?email=${forgotPasswordRequest.email}`);
    }

    getCurrentUser(): Observable<User> {
        return this.httpClient.get<User>(`${this.baseUrl}/auth/current-user`);
    }

    setCurrentUser(user: User | null) {
        this.currentUser.set(user);
    }

    setToken(token: string) {
        localStorage.setItem(this.tokenKey, token);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    removeToken(): void {
        localStorage.removeItem(this.tokenKey);
    }

    isLoggedIn(): boolean {
        return this.currentUser() !== null;
    }

    isAdmin(): boolean {
        return this.currentUser()?.profils?.some((p) => p.name === 'ROLE_ADMIN') || false;
    }

    isSeller(): boolean {
        return this.currentUser()?.profils?.some((p) => p.name === 'ROLE_SELLER') || false;
    }

    isTokenExpired(): boolean {
        const token = this.getToken();
        if (!token) {
            return true;
        }
        const payloadBase64 = token.split('.')[1];
        const payload = JSON.parse(atob(payloadBase64));
        const expiry = payload.exp;

        if (!expiry) {
            return true;
        }
        const now = Math.floor(Date.now() / 1000);
        return now >= expiry;
    }
}
