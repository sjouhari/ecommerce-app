import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [CommonModule, AppMenuitem, RouterModule, ConfirmDialogModule],
    providers: [ConfirmationService],
    template: `<ul class="layout-menu">
            <ng-container *ngFor="let item of model; let i = index">
                <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
                <li *ngIf="item.separator" class="menu-separator"></li>
            </ng-container>
        </ul>
        <p-confirmdialog [style]="{ width: '600px' }" />`
})
export class AppMenu {
    model: MenuItem[] = [];

    authService = inject(AuthService);
    confirmationService = inject(ConfirmationService);

    ngOnInit() {
        this.model = [
            {
                items: [
                    {
                        label: 'Dashboard',
                        icon: 'pi pi-fw pi-home',
                        routerLink: ['/']
                    },
                    {
                        label: 'Gestion des produits',
                        icon: 'pi pi-fw pi-box',
                        routerLink: ['/products']
                    },
                    {
                        label: 'Gestion des magasins',
                        icon: 'pi pi-fw pi-shop',
                        routerLink: ['/stores']
                    },
                    {
                        label: 'Gestion des catégories',
                        icon: 'pi pi-fw pi-tags',
                        items: [
                            { label: 'Catégories', icon: 'pi pi-fw pi-sitemap', routerLink: ['/categories'] },
                            { label: 'Sous-catégories', icon: 'pi pi-fw pi-angle-double-right', routerLink: ['/subcategories'] },
                            { label: 'Tailles', icon: 'pi pi-fw pi-tags', routerLink: ['/sizes'] }
                        ]
                    },
                    {
                        label: 'Commandes',
                        icon: 'pi pi-fw pi-shopping-cart',
                        items: [
                            { label: 'Toutes les commandes', icon: 'pi pi-fw pi-list', routerLink: ['/orders'] },
                            { label: 'En attente', icon: 'pi pi-fw pi-clock', routerLink: ['/orders/pending'] },
                            { label: 'Expédiées', icon: 'pi pi-fw pi-send', routerLink: ['/orders/shipped'] },
                            { label: 'Retours', icon: 'pi pi-fw pi-refresh', routerLink: ['/orders/returns'] }
                        ]
                    },
                    {
                        label: 'Gestion des utilisateurs',
                        icon: 'pi pi-fw pi-users',
                        items: [
                            { label: 'Toutes les utilisateurs', icon: 'pi pi-fw pi-list', routerLink: ['/users'] },
                            { label: 'Clients', icon: 'pi pi-fw pi-clock', routerLink: ['/clients'] },
                            { label: 'Administrateurs', icon: 'pi pi-fw pi-ticket', routerLink: ['/admins'] },
                            { label: 'Vendeurs', icon: 'pi pi-fw pi-briefcase', routerLink: ['/vendors'] }
                        ]
                    },
                    {
                        label: 'Gestion des permissions',
                        icon: 'pi pi-fw pi-lock',
                        items: [
                            { label: 'Profils', icon: 'pi pi-fw pi-id-card', routerLink: ['/roles'] },
                            { label: 'Fonctionnalités', icon: 'pi pi-fw pi-th-large', routerLink: ['/features'] }
                        ]
                    },
                    {
                        label: 'Déconnexion',
                        icon: 'pi pi-fw pi-sign-out',
                        command: () => {
                            this.confirmationService.confirm({
                                message: 'Êtes-vous sûr de vouloir vous déconnecter ?',
                                header: 'Déconnexion',
                                icon: 'pi pi-exclamation-triangle',
                                accept: () => {
                                    this.authService.logout();
                                }
                            });
                        }
                    }
                ]
            }
        ];

        if (!this.authService.isAdmin()) {
            this.model[0]?.items?.splice(2, 1);
            this.model[0]?.items?.splice(4, 1);
            this.model[0]?.items?.splice(4, 1);
        }
    }
}
