import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Notfound } from './app/pages/notfound/notfound';
import { LoginComponent } from './app/pages/auth/login/login.component';
import { RegisterComponent } from './app/pages/auth/register/register.component';
import { ForgotPasswordComponent } from './app/pages/auth/forgot-password/forgot-password.component';
import { authGuard } from './app/guards/auth.guard';
import { ProductsComponent } from './app/pages/products/products.component';
import { UsersComponent } from './app/pages/users/users.component';
import { CategoriesComponent } from './app/pages/categories/categories.component';
import { SubCategoriesComponent } from './app/pages/subcategories/sub-categories.component';
import { ProfilesComponent } from './app/pages/profiles/profiles.component';
import { SizesComponent } from './app/pages/sizes/sizes.component';
import { FeaturesComponent } from './app/pages/features/features.component';
import { ListProductsComponent } from './app/pages/list-products/list-products.component';
import { ProductDetailsComponent } from './app/pages/product-details/product-details.component';
import { ShoppingCartComponent } from './app/pages/shopping-cart/shopping-cart.component';
import { OrderSummaryComponent } from './app/pages/order-summary/order-summary.component';
import { OrdersComponent } from './app/pages/orders/orders.component';
import { UserProfileComponent } from './app/pages/user-profile/user-profile.component';
import { UserLayoutComponent } from './app/pages/user-layout/user-layout.component';
import { HomeComponent } from './app/pages/user-layout/components/home/home.component';

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
            { path: 'inventory', component: ProductsComponent },

            // Categories
            { path: 'categories', component: CategoriesComponent },
            { path: 'subcategories', component: SubCategoriesComponent },
            { path: 'sizes', component: SizesComponent },

            // Commandes
            { path: 'orders', component: OrdersComponent },
            { path: 'orders/pending', component: OrdersComponent },
            { path: 'orders/shipped', component: OrdersComponent },
            { path: 'orders/returns', component: OrdersComponent },

            // Utilisateurs
            { path: 'users', component: UsersComponent, data: { role: 'all' } },
            { path: 'clients', component: UsersComponent, data: { role: 'client' } },
            { path: 'admins', component: UsersComponent, data: { role: 'admin' } },
            { path: 'vendors', component: UsersComponent, data: { role: 'vendor' } },

            // Permissions et rôles
            { path: 'roles', component: ProfilesComponent },
            { path: 'features', component: FeaturesComponent },
            { path: 'profile', component: UserProfileComponent },

            // // Promotions
            // { path: 'coupons', component: ProductsComponent },
            // { path: 'discounts', component: ProductsComponent },

            // // Rapports
            // { path: 'reports/sales', component: ProductsComponent },
            // { path: 'reports/products', component: ProductsComponent },
            // { path: 'reports/customers', component: ProductsComponent },

            // Paramètres
            { path: 'settings', component: ProductsComponent }
        ]
    },
    {
        path: 'home',
        component: UserLayoutComponent,
        children: [
            { path: '', component: HomeComponent },
            { path: 'list-products', component: ListProductsComponent },
            { path: 'list-products/:id', component: ProductDetailsComponent },
            { path: 'shopping-cart', component: ShoppingCartComponent },
            { path: 'order-summary/:id', component: OrderSummaryComponent },
            { path: 'profile', component: UserProfileComponent }
        ]
    },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: '/notfound' }
];
