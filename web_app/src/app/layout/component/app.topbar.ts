import { Component, inject } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { LayoutService } from '../service/layout.service';
import { ChipModule } from 'primeng/chip';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [RouterModule, CommonModule, StyleClassModule, ChipModule],
    template: ` <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" (click)="layoutService.onMenuToggle()">
                <i class="pi pi-bars"></i>
            </button>
            <a class="flex items-center mr-6" routerLink="/">
                <img src="assets/logo/app-logo.png" class="h-2rem" alt="APP LOGO" />
            </a>
            <a routerLink="" class="flex items-center mr-6">Espace Utilisateur</a>
        </div>

        <div class="layout-topbar-actions">
            <div class="layout-config-menu">
                <button type="button" class="layout-topbar-action" (click)="toggleDarkMode()">
                    <i [ngClass]="{ 'pi text-xl ': true, 'pi-moon': layoutService.isDarkTheme(), 'pi-sun': !layoutService.isDarkTheme() }"></i>
                </button>
            </div>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <a routerLink="profile"><p-chip [label]="getUserFullName()" image="https://cdn4.iconfinder.com/data/icons/ecommerce-flat-8/64/avatar_man_think_shopping_ecommerce-512.png" styleClass="px-3 py-2"></p-chip></a>
                </div>
            </div>
        </div>
    </div>`
})
export class AppTopbar {
    layoutService = inject(LayoutService);
    authService = inject(AuthService);

    items!: MenuItem[];
    currentUser!: string;

    getUserFullName() {
        return this.authService.currentUser()?.firstName + ' ' + this.authService.currentUser()?.lastName;
    }

    toggleDarkMode() {
        this.layoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }
}
