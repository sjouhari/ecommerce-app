import { Component, inject, OnInit, signal } from '@angular/core';
import { StyleClassModule } from 'primeng/styleclass';
import { Router, RouterModule } from '@angular/router';
import { RippleModule } from 'primeng/ripple';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { Popover, PopoverModule } from 'primeng/popover';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { Product } from '../../../models/product/product.model';
import { MessageService } from 'primeng/api';
import { OverlayBadgeModule } from 'primeng/overlaybadge';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models/category/category.model';
import { SubCategory } from '../../../models/category/sub-category.model';
import { SubCategoryService } from '../../../services/sub-category.service';
import { ShoppingCartService } from '../../../services/shopping-cart.service';
import { ShoppingCart } from '../../../models/order/shopping-cart.model';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'topbar-widget',
    providers: [MessageService],
    imports: [RouterModule, StyleClassModule, ButtonModule, RippleModule, ChipModule, PopoverModule, TableModule, ToastModule, OverlayBadgeModule],
    template: `
        <a class="flex items-center mr-6" href="#">
            <img src="assets/logo/app-logo.png" class="h-3rem" alt="APP LOGO" />
        </a>

        <div class="items-center bg-surface-0 dark:bg-surface-900 grow justify-between hidden lg:flex absolute lg:static w-full left-0 top-full px-12 lg:px-0 z-20 rounded-border">
            <ul class="list-none p-0 m-0 flex lg:items-center select-none flex-col lg:flex-row cursor-pointer gap-5">
                <li>
                    <a (click)="router.navigate(['/home'], { fragment: 'home' })" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                        <span>Home</span>
                    </a>
                </li>
                <li>
                    <div class="flex flex-wrap gap-2 align-items-center">
                        <a (mousedown)="toggleDataTable(cat, $event)" (mouseenter)="toggleDataTable(cat, $event)" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">Catégories</a>
                        <p-popover (mousedown)="toggleDataTable(cat, $event)" #cat id="categories_popover" [style]="{ width: '60%' }">
                            @if (categories().length > 0) {
                                <ul class="grid grid-cols-4 p-3 cursor-pointer">
                                    @for (category of categories(); track category.id) {
                                        <li class="text-surface-900 pb-2">
                                            <a (click)="router.navigate(['/list-products'], { fragment: 'home' })" pRipple class="py-3 px-0 font-medium text-xl hover:text-primary">{{ category.name }}</a>
                                            <ul>
                                                @for (subCategory of category.subCategories; track subCategory.id) {
                                                    <li class="text-surface-900 py-2 ml-3">
                                                        <a href="#" class=" hover:text-primary">{{ subCategory.name }}</a>
                                                    </li>
                                                }
                                            </ul>
                                        </li>
                                    }
                                </ul>
                            } @else {
                                <p>Aucune catégorie</p>
                            }
                        </p-popover>
                        <p-toast />
                    </div>
                </li>
                <li>
                    <a (click)="router.navigate(['/home'], { fragment: 'new-products' })" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                        <span>Nouveaux arrivages</span>
                    </a>
                </li>
                <li>
                    <a (click)="router.navigate(['/home'], { fragment: 'new-products' })" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                        <span>Nos produits</span>
                    </a>
                </li>
            </ul>
            <div class="flex border-t lg:border-t-0 border-surface py-4 lg:py-0 mt-4 lg:mt-0 gap-2">
                <!-- <button pButton pRipple label="Login" routerLink="/login" [rounded]="true" [text]="true"></button>
                <button pButton pRipple label="Register" routerLink="/register" [rounded]="true"></button> -->
                <div class="flex flex-wrap gap-2 align-items-center">
                    <p-overlaybadge class="mr-3 cursor-pointer" [value]="shoppingCart() ? shoppingCart()!.orderItems.length : 0" (click)="toggleDataTable(op2, $event)">
                        <i class="pi pi-shopping-cart text-4xl"></i>
                    </p-overlaybadge>
                    <p-popover #op2 id="overlay_panel" [style]="{ width: '600px' }">
                        @if (shoppingCart() && shoppingCart()!.orderItems.length > 0) {
                            <p-table [value]="shoppingCart()!.orderItems" selectionMode="single" dataKey="id" [rows]="5" [paginator]="true">
                                <ng-template #header>
                                    <tr>
                                        <th>Image</th>
                                        <th>Nom</th>
                                        <th>Taille</th>
                                        <th>Couleur</th>
                                        <th>Quantité</th>
                                        <th>Prix</th>
                                    </tr>
                                </ng-template>
                                <ng-template #body let-item>
                                    <tr [pSelectableRow]="item">
                                        <td><img [src]="'https://primefaces.org/cdn/primeng/images/demo/product/'" class="w-16 shadow-sm" /></td>
                                        <td>{{ item.productName }}</td>
                                        <td>{{ item.size }}</td>
                                        <td>{{ item.color }}</td>
                                        <td>{{ item.quantity }}</td>
                                        <td>{{ item.price * item.quantity }}</td>
                                    </tr>
                                </ng-template>
                            </p-table>
                            <a pButton routerLink="shopping-cart" label="Accéder au panier" class="w-full py-2" severity="contrast"></a>
                        } @else {
                            <p>Aucun produit dans le panier</p>
                        }
                    </p-popover>
                    <p-toast />
                </div>
                <a href="#"><p-chip label="SAID JOUHARI" image="https://primefaces.org/cdn/primeng/images/demo/avatar/amyelsner.png" styleClass="px-3 py-2"></p-chip></a>
            </div>
        </div>
    `
})
export class TopbarWidget implements OnInit {
    router = inject(Router);
    categoryService = inject(CategoryService);
    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);

    categories = signal<Category[]>([]);
    shoppingCart = signal<ShoppingCart | null>(null);

    ngOnInit() {
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });

        this.shoppingCartService.getShoppingCart(this.authService.getCurrentUser()?.id!).subscribe({
            next: (cart) => {
                this.shoppingCart.set(cart);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    toggleDataTable(op: Popover, event: any) {
        op.toggle(event);
    }
}
