import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-initializer',
    imports: [],
    templateUrl: './initializer.component.html'
})
export class InitializerComponent {
    authService = inject(AuthService);
    router = inject(Router);

    constructor() {
        if (!this.authService.getToken()) {
            this.router.navigate(['/home']);
            return;
        }
        this.authService.getCurrentUser().subscribe({
            next: (user) => {
                this.authService.setCurrentUser(user);
                if (this.authService.isLoggedIn() && (this.authService.isAdmin() || this.authService.isSeller())) {
                    this.router.navigate(['/']);
                } else {
                    this.router.navigate(['/home']);
                }
            },
            error: (error) => {
                this.authService.setCurrentUser(null);
                this.authService.logout();
                this.router.navigate(['/home']);
            }
        });
    }
}
