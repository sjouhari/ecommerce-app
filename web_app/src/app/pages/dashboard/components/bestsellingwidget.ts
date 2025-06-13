import { AuthService } from './../../../services/auth.service';
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { OrderService } from '../../../services/order.service';
import { BestSellingProduct } from '../../../models/order/best-selling-product.model';

@Component({
    standalone: true,
    selector: 'app-best-selling-widget',
    imports: [CommonModule, ButtonModule, MenuModule],
    template: ` <div class="card">
        <div class="flex justify-between items-center mb-6">
            <div class="font-semibold text-xl">Les produits les plus vendus</div>
        </div>
        <ul class="list-none p-0 m-0">
            @for (product of products; track product.productId) {
                <li class="flex flex-col md:flex-row md:items-center md:justify-between mb-6 border-bottom-1 border-surface">
                    <div class="flex items-center">
                        <img src="http://localhost:8080/api/products/images/{{ product.productImage }}" class="w-10rem md:w-6rem" alt="Image" />
                        <span class="text-surface-900 dark:text-surface-0 font-medium mr-2 mb-1 md:mb-0">{{ product.productName }}</span>
                    </div>
                    <div class="mt-2 md:mt-0 flex items-center">
                        <span class="text-orange-500 ml-4 font-medium">{{ product.totalSold }}</span>
                    </div>
                </li>
            } @empty {
                <li>
                    <div>Aucun produit vendu</div>
                </li>
            }
        </ul>
    </div>`
})
export class BestSellingWidget implements OnInit {
    orderService = inject(OrderService);
    authService = inject(AuthService);

    products: BestSellingProduct[] = [];

    ngOnInit() {
        if (this.authService.isAdmin()) {
            this.orderService.getBestSellingProducts().subscribe({
                next: (products) => {
                    this.products = products;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.orderService.getBestSellingProductsByStoreId(this.authService.currentUser()?.store?.id!).subscribe({
                next: (products) => {
                    this.products = products;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
