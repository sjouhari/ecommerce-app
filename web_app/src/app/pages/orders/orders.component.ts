import { Component, inject } from '@angular/core';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { RatingModule } from 'primeng/rating';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order/order.model';
import { DialogModule } from 'primeng/dialog';
import { IftaLabelModule } from 'primeng/iftalabel';
import { OrderStatus } from '../../models/order/order-status';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-orders',
    imports: [TableModule, FormsModule, TagModule, ToastModule, RatingModule, ButtonModule, CommonModule, DialogModule, IftaLabelModule, RadioButtonModule],
    templateUrl: './orders.component.html',
    providers: [MessageService]
})
export class OrdersComponent {
    orderService = inject(OrderService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    route = inject(ActivatedRoute);
    orders: Order[] = [];

    orderStatusDialog = false;

    expandedRows = {};

    currentStatus: string = '';
    selectedStatus: string = '';

    selectedOrderId: number = 0;
    loading = false;

    statuses = Object.entries(OrderStatus).map(([key, value]) => ({ label: value, value: key }));

    ngOnInit() {
        this.getOrders();
    }

    getOrders() {
        if (this.authService.isAdmin()) {
            this.orderService.getOrders().subscribe({
                next: (orders) => {
                    this.route.data.subscribe((data) => {
                        const status = data['status'];
                        switch (status) {
                            case 'all':
                                this.orders = orders;
                                break;
                            case 'pending':
                                this.orders = orders.filter((order) => order.status === 'PENDING');
                                break;
                            case 'processing':
                                this.orders = orders.filter((order) => order.status === 'PROCESSING');
                                break;
                            case 'shipped':
                                this.orders = orders.filter((order) => order.status === 'SHIPPED');
                                break;
                            case 'delivered':
                                this.orders = orders.filter((order) => order.status === 'DELIVERED');
                                break;
                            case 'cancelled':
                                this.orders = orders.filter((order) => order.status === 'CANCELLED');
                                break;
                            default:
                                this.orders = orders;
                                break;
                        }
                    });
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

    getNextStatus() {
        switch (this.currentStatus) {
            case 'PENDING':
                return 'PROCESSING';
            case 'PROCESSING':
                return 'SHIPPED';
            case 'SHIPPED':
                return 'DELIVERED';
            case 'DELIVERED':
                return '';
            default:
                return 'PENDING';
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

    showStatusDialog(order: Order) {
        this.loading = false;
        this.selectedOrderId = order.id!;
        this.currentStatus = order.status!;
        this.selectedStatus = '';
        this.orderStatusDialog = true;
    }

    hideDialog() {
        this.loading = false;
        this.selectedStatus = '';
        this.currentStatus = '';
        this.selectedOrderId = 0;
        this.orderStatusDialog = false;
    }

    updateOrderStatus() {
        this.loading = true;
        if (!this.selectedStatus && this.selectedStatus === this.currentStatus) return;
        this.orderService.updateOrderStatus(this.selectedOrderId, this.selectedStatus).subscribe({
            next: (order) => {
                this.getOrders();
                this.hideDialog();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Statut mis à jour',
                    detail: '',
                    life: 3000
                });
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
}
