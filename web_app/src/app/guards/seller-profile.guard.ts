import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const sellerProfileGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);

    return authService.isSeller();
};
