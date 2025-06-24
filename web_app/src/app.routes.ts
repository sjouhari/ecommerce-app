import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Notfound } from './app/pages/notfound/notfound';
import { authGuard } from './app/guards/auth.guard';
import { ProductsComponent } from './app/pages/products/products.component';
import { UsersComponent } from './app/pages/users/users.component';
import { CategoriesComponent } from './app/pages/categories/categories.component';
import { SubCategoriesComponent } from './app/pages/subcategories/sub-categories.component';
import { ProfilesComponent } from './app/pages/profiles/profiles.component';
import { SizesComponent } from './app/pages/sizes/sizes.component';
import { FeaturesComponent } from './app/pages/features/features.component';
import { OrdersComponent } from './app/pages/orders/orders.component';
import { sellerProfileGuard } from './app/guards/seller-profile.guard';
import { ProductDetailsComponent } from './app/pages/product-details/product-details.component';
import { StoresComponent } from './app/pages/stores/stores.component';
import { UserProfileComponent } from './app/pages/user-profile/user-profile.component';
import { UserLayoutComponent } from './app/user-layout/user-layout.component';
import { HomeComponent } from './app/pages/home/home.component';
import { LoginComponent } from './app/pages/auth/login/login.component';
import { RegisterComponent } from './app/pages/auth/register/register.component';
import { ForgotPasswordComponent } from './app/pages/auth/forgot-password/forgot-password.component';
import { ListProductsComponent } from './app/pages/list-products/list-products.component';
import { StoreProductsComponent } from './app/pages/store-products/store-products.component';
import { CategoryProductsComponent } from './app/pages/category-products/category-products.component';
import { ShoppingCartComponent } from './app/pages/shopping-cart/shopping-cart.component';
import { OrderSummaryComponent } from './app/pages/order-summary/order-summary.component';
import { ContactComponent } from './app/pages/contact/contact.component';
import { ContactsComponent } from './app/pages/contacts/contacts.component';

export const appRoutes: Routes = [
    {
        path: '',
        component: UserLayoutComponent,
        children: [
            { path: '', component: HomeComponent },
            { path: 'login', component: LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: 'forgot-password', component: ForgotPasswordComponent },
            { path: 'list-products', component: ListProductsComponent },
            { path: 'list-products/:id', component: ProductDetailsComponent },
            { path: 'stores/:id', component: StoreProductsComponent },
            { path: 'category-products/:id', component: CategoryProductsComponent },
            { path: 'shopping-cart', component: ShoppingCartComponent },
            { path: 'order-summary/:id', component: OrderSummaryComponent },
            { path: 'profile', component: UserProfileComponent },
            { path: 'contact', component: ContactComponent }
        ]
    },
    {
        path: 'admin',
        component: AppLayout,
        canActivate: [authGuard, sellerProfileGuard],
        children: [
            { path: '', component: Dashboard },

            // Produits
            { path: 'products', component: ProductsComponent },
            { path: 'products/:id', component: ProductDetailsComponent },
            { path: 'inventory', component: ProductsComponent },

            { path: 'stores', component: StoresComponent },

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
            { path: 'contacts', component: ContactsComponent }
        ]
    },
    { path: 'notfound', component: Notfound },
    { path: '**', redirectTo: '/notfound' }
];
