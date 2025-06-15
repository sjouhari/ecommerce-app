import { $t } from '@primeng/themes';
import { Component, inject } from '@angular/core';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../services/product.service';
import { Product } from '../../../models/product/product.model';
import { OrderService } from '../../../services/order.service';
import { OrderItem } from '../../../models/order/order-item.model';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
    standalone: true,
    selector: 'app-recent-sales-widget',
    imports: [CommonModule, TableModule, ButtonModule, RippleModule, RouterLink],
    template: `<div class="card !mb-8">
        <div class="font-semibold text-xl mb-4">Ventes récentes</div>
        <p-table [value]="products" [paginator]="true" [rows]="5" responsiveLayout="scroll">
            <ng-template #header>
                <tr>
                    <th>Image</th>
                    <th pSortableColumn="name">Nom <p-sortIcon field="name"></p-sortIcon></th>
                    <th pSortableColumn="price">Prix <p-sortIcon field="price"></p-sortIcon></th>
                    <th>Voir</th>
                </tr>
            </ng-template>
            <ng-template #body let-product>
                <tr>
                    <td style="width: 15%; min-width: 5rem;">
                        <img [src]="'http://localhost:8080/api/products/images/' + product.productImage" class="shadow-lg" alt="{{ product.name }}" width="50" />
                    </td>
                    <td style="width: 35%; min-width: 7rem;">{{ product.productName }}</td>
                    <td style="width: 35%; min-width: 8rem;">{{ product.price | currency: 'MAD ' }}</td>
                    <td style="width: 15%;">
                        <a [routerLink]="'/list-products/' + product.productId"><i class="pi pi-eye"></i></a>
                    </td>
                </tr>
            </ng-template>
            <ng-template #emptymessage>
                <tr>
                    <td colspan="6">Aucune vente pour le moment</td>
                </tr>
            </ng-template>
        </p-table>
    </div>`,
    providers: [ProductService]
})
export class RecentSalesWidget {
    products: OrderItem[] = [];

    orderService = inject(OrderService);
    authService = inject(AuthService);

    ngOnInit() {
        if (this.authService.isAdmin()) {
            this.orderService.getOrders().subscribe({
                next: (orders) => {
                    this.products = Array.from(
                        new Set(
                            orders
                                .map((order) => order.orderItems)
                                .flat()
                                .map((item) => item.productId)
                                .map((id) => orders.flatMap((order) => order.orderItems).find((item) => item.productId === id)!)
                        )
                    );
                },
                error: (error) => {
                    console.error(error);
                }
            });
        } else {
            this.orderService.getOrdersByStoreId(this.authService.currentUser()?.store?.id!).subscribe({
                next: (orders) => {
                    this.products = Array.from(
                        new Set(
                            orders
                                .map((order) => order.orderItems)
                                .flat()
                                .map((item) => item.productId)
                                .map((id) => orders.flatMap((order) => order.orderItems).find((item) => item.productId === id)!)
                        )
                    );
                },
                error: (error) => {
                    console.error(error);
                }
            });
        }
    }
}
