import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { Crud } from './app/pages/crud/crud';
import { LoginComponent } from './app/pages/auth/login/login.component';
import { RegisterComponent } from './app/pages/auth/register/register.component';
import { ForgotPasswordComponent } from './app/pages/auth/forgot-password/forgot-password.component';
import { authGuard } from './app/guards/auth.guard';
import { ProductsComponent } from './app/pages/products/products.component';
import { UsersComponent } from './app/pages/users/users.component';
import { CategoriesComponent } from './app/pages/categories/categories.component';
import { SubCategoriesComponent } from './app/pages/subcategories/sub-categories.component';
import { ProfilsComponent } from './app/pages/profils/profils.component';
import { SizesComponent } from './app/pages/sizes/sizes.component';

export const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'forgot-password', component: ForgotPasswordComponent },
    {
        path: '',
        component: AppLayout,
        // canActivate: [authGuard],
        children: [
            { path: '', component: Dashboard },

            // Produits
            { path: 'products', component: ProductsComponent },
            { path: 'inventory', component: Crud },

            // Categories
            { path: 'categories', component: CategoriesComponent },
            { path: 'subcategories', component: SubCategoriesComponent },
            { path: 'sizes', component: SizesComponent },

            // Commandes
            { path: 'orders', component: Crud },
            { path: 'orders/pending', component: Crud },
            { path: 'orders/shipped', component: Crud },
            { path: 'orders/returns', component: Crud },

            // Utilisateurs
            { path: 'users', component: UsersComponent },
            { path: 'admins', component: Crud },
            { path: 'vendors', component: Crud },

            // Permissions et rôles
            { path: 'roles', component: ProfilsComponent },
            { path: 'features', component: Crud },

            // Promotions
            { path: 'coupons', component: Crud },
            { path: 'discounts', component: Crud },

            // Rapports
            { path: 'reports/sales', component: Crud },
            { path: 'reports/products', component: Crud },
            { path: 'reports/customers', component: Crud },

            // Paramètres
            { path: 'settings', component: Crud }
        ]
    },
    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: '/notfound' }
];
