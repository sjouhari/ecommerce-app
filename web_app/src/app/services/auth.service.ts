import { User } from '../models/user/user.model';
import { ForgotPasswordRequest } from './../models/auth/forgot-password.model';
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/auth/login.model';
import { RegisterRequest, RegisterResponse } from '../models/auth/register.model';
import { ForgotPasswordResponse } from '../models/auth/forgot-password.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    httpClient = inject(HttpClient);

    private tokenKey = 'token';

    private baseUrl = 'http://localhost:8080/api';

    login(loginRequest: LoginRequest): Observable<LoginResponse> {
        return this.httpClient.post<LoginResponse>(`${this.baseUrl}/auth/login`, loginRequest);
    }

    register(registerRequest: RegisterRequest): Observable<RegisterResponse> {
        console.log(registerRequest);
        return this.httpClient.post<RegisterResponse>(`${this.baseUrl}/auth/register`, registerRequest);
    }

    logout(): void {
        localStorage.removeItem(this.tokenKey);
    }

    forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Observable<ForgotPasswordResponse> {
        return this.httpClient.get<ForgotPasswordResponse>(`${this.baseUrl}/users/forgot-password?email=${forgotPasswordRequest.email}`);
    }

    setToken(token: string) {
        localStorage.setItem(this.tokenKey, token);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isLoggedIn(): boolean {
        const token = this.getToken();
        return !!token && !this.isTokenExpired(token!);
    }

    getCurrentUser(): User | null {
        const token = this.getToken();
        if (!token) {
            return null;
        }
        const payloadBase64 = token.split('.')[1];
        const payload = JSON.parse(atob(payloadBase64));
        const currentUser: User = payload.user;
        return currentUser;
    }

    isTokenExpired(token: string): boolean {
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
