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
import { DialogModule } from 'primeng/dialog';
import { IftaLabelModule } from 'primeng/iftalabel';
import { Select } from 'primeng/select';
import { OrderStatus } from '../../models/order/order-status';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-orders',
    imports: [TableModule, FormsModule, TagModule, ToastModule, RatingModule, ButtonModule, CommonModule, DialogModule, IftaLabelModule, Select],
    templateUrl: './orders.component.html',
    providers: [MessageService]
})
export class OrdersComponent {
    orderService = inject(OrderService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    orders: Order[] = [];

    orderStatusDialog = false;

    expandedRows = {};

    selectedStatus: string = '';

    selectedOrderId: number = 0;

    statuses = Object.entries(OrderStatus).map(([key, value]) => ({ label: value, value: key }));

    ngOnInit() {
        this.getOrders();
    }

    getOrders() {
        if (this.authService.isAdmin()) {
            this.orderService.getOrders().subscribe({
                next: (orders) => {
                    this.orders = orders;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.orderService.getOrdersByStoreId(this.authService.currentUser()?.store?.id!).subscribe({
                next: (orders) => {
                    this.orders = orders;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
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

    updateOrderStatus() {
        this.orderService.updateOrderStatus(this.selectedOrderId, this.selectedStatus).subscribe({
            next: (order) => {
                this.getOrders();
                this.hideDialog();
                this.messageService.add({ severity: 'success', summary: 'Statut mis à jour', detail: '', life: 3000 });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    confirmOrderPayment(id: number) {
        this.orderService.confirmOrderPayment(id).subscribe({
            next: (order) => {
                this.getOrders();
                this.messageService.add({ severity: 'success', summary: 'Le paiement a été confirmé', detail: '', life: 3000 });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    showStatusDialog(order: Order) {
        this.selectedOrderId = order.id!;
        this.selectedStatus = order.status!;
        this.orderStatusDialog = true;
    }

    hideDialog() {
        this.selectedStatus = '';
        this.selectedOrderId = 0;
        this.orderStatusDialog = false;
    }
}
