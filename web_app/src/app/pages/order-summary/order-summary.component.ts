import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order/order.model';
import { PaymentMethods } from '../../models/order/payment-methods';

@Component({
    selector: 'app-order-summary',
    imports: [],
    templateUrl: './order-summary.component.html'
})
export class OrderSummaryComponent implements OnInit {
    activatedRoute = inject(ActivatedRoute);
    orderService = inject(OrderService);

    order = signal<Order | null>(null);
    paymentMethods = Object.entries(PaymentMethods).map(([key, value]) => ({ key, value }));

    ngOnInit(): void {
        let id = this.activatedRoute.snapshot.paramMap.get('id');
        this.orderService.getOrder(id!).subscribe({
            next: (order) => {
                this.order.set(order);
            },
            error: (error) => console.error(error)
        });
    }
}
