import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
    selector: 'app-order-summary',
    imports: [],
    templateUrl: './order-summary.component.html'
})
export class OrderSummaryComponent implements OnInit {
    activatedRoute = inject(ActivatedRoute);
    orderService = inject(OrderService);

    ngOnInit(): void {
        let id = this.activatedRoute.snapshot.paramMap.get('id');
        this.orderService.getOrder(id!).subscribe({
            next: (order) => console.log(order),
            error: (error) => console.error(error)
        });
    }
}
