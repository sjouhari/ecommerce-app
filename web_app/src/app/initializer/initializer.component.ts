import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
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
        this.authService.getCurrentUser().subscribe({
            next: (user) => {
                this.authService.setCurrentUser(user);
                this.router.navigate(['/']);
            },
            error: () => {
                this.authService.setCurrentUser(null);
                this.router.navigate(['/home']);
            }
        });
    }
}
