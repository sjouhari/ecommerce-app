import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order/order.model';
import { PaymentMethods } from '../../models/order/payment-methods';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-order-summary',
    imports: [ButtonModule],
    templateUrl: './order-summary.component.html'
})
export class OrderSummaryComponent implements OnInit {
    activatedRoute = inject(ActivatedRoute);
    orderService = inject(OrderService);

    order = signal<Order | null>(null);
    paymentMethods = Object.entries(PaymentMethods).map(([key, value]) => ({ key, value }));
    loading = signal(false);

    ngOnInit(): void {
        let id = this.activatedRoute.snapshot.paramMap.get('id');
        this.orderService.getOrder(id!).subscribe({
            next: (order) => {
                this.order.set(order);
            },
            error: (error) => console.error(error)
        });
    }

    downloadInvoice() {
        this.loading.set(true);
        let orderId = this.activatedRoute.snapshot.paramMap.get('id');
        this.orderService.downloadInvoice(+orderId!).subscribe({
            next: (pdfBlob) => {
                const blob = new Blob([pdfBlob], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);

                const link = document.createElement('a');
                link.href = url;
                link.download = `commande_${orderId}.pdf`;
                link.click();

                window.URL.revokeObjectURL(url);
                this.loading.set(false);
            },
            error: (error) => {
                this.loading.set(false);
                console.error(error); //TODO: handle error
            }
        });
    }
}
