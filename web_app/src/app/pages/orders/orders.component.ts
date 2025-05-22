import { Component, inject, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { RatingModule } from 'primeng/rating';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { TableRowCollapseEvent, TableRowExpandEvent } from 'primeng/table';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order/order.model';

@Component({
    selector: 'app-orders',
    imports: [TableModule, TagModule, ToastModule, RatingModule, ButtonModule, CommonModule],
    templateUrl: './orders.component.html',
    providers: [MessageService]
})
export class OrdersComponent {
    orderService = inject(OrderService);
    messageService = inject(MessageService);
    orders!: Order[];

    expandedRows = {};

    ngOnInit() {
        this.orderService.getOrders().subscribe({
            next: (orders) => {
                this.orders = orders;
                console.log(this.orders);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    expandAll() {
        this.expandedRows = this.orders.reduce(
            (acc, o) => {
                acc[o.id!] = true;
                return acc;
            },
            {} as { [key: number]: boolean }
        );
    }

    collapseAll() {
        this.expandedRows = {};
    }

    getSeverity(status: string) {
        switch (status) {
            case 'DELIVERED':
                return 'success';
            case 'PENDING':
                return 'warn';
            case 'PROCESSING':
                return 'danger';
            default:
                return 'info';
        }
    }

    getStatusSeverity(status: string) {
        switch (status) {
            case 'PENDING':
                return 'warn';
            case 'DELIVERED':
                return 'success';
            case 'CANCELLED':
                return 'danger';
            default:
                return 'info';
        }
    }

    onRowExpand(event: TableRowExpandEvent) {
        this.messageService.add({ severity: 'info', summary: 'Product Expanded', detail: event.data.name, life: 3000 });
    }

    onRowCollapse(event: TableRowCollapseEvent) {
        this.messageService.add({ severity: 'success', summary: 'Product Collapsed', detail: event.data.name, life: 3000 });
    }
}
