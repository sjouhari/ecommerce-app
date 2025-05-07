import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
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
import { FeaturesComponent } from './app/pages/features/features.component';
import { HomeComponent } from './app/pages/home/home.component';
import { ListProductsComponent } from './app/pages/list-products/list-products.component';

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
            { path: 'users', component: UsersComponent, data: { role: 'user' } },
            { path: 'admins', component: UsersComponent, data: { role: 'admin' } },
            { path: 'vendors', component: UsersComponent, data: { role: 'vendor' } },

            // Permissions et rôles
            { path: 'roles', component: ProfilsComponent },
            { path: 'features', component: FeaturesComponent },

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
    { path: 'landing', component: HomeComponent },
    { path: 'list-products', component: ListProductsComponent },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: '/notfound' }
];
