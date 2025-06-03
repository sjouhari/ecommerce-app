import { Component, inject, OnInit, signal } from '@angular/core';
import { StyleClassModule } from 'primeng/styleclass';
import { Router, RouterModule } from '@angular/router';
import { RippleModule } from 'primeng/ripple';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';
import { Popover, PopoverModule } from 'primeng/popover';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { OverlayBadgeModule } from 'primeng/overlaybadge';
import { CategoryService } from '../../../../services/category.service';
import { Category } from '../../../../models/category/category.model';
import { ShoppingCartService } from '../../../../services/shopping-cart.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
    selector: 'app-navbar',
    imports: [RouterModule, StyleClassModule, ButtonModule, RippleModule, ChipModule, PopoverModule, TableModule, ToastModule, OverlayBadgeModule],
    templateUrl: './navbar.component.html',
    providers: [MessageService]
})
export class NavbarComponent implements OnInit {
    router = inject(Router);
    categoryService = inject(CategoryService);
    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);

    categories = signal<Category[]>([]);

    ngOnInit() {
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getFullName() {
        return this.authService.currentUser()?.firstName + ' ' + this.authService.currentUser()?.lastName;
    }

    toggleDataTable(op: Popover, event: any) {
        op.toggle(event);
    }

    gotoCart(op: Popover, event: any) {
        this.toggleDataTable(op, event);
        this.router.navigate(['/home/shopping-cart']);
    }
}
