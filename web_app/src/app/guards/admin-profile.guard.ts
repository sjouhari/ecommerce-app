import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminProfileGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);

    return authService.isAdmin();
};
