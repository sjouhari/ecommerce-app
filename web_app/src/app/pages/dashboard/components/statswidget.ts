import { last } from 'rxjs';
import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order.service';
import { Order } from '../../../models/order/order.model';
import { OrderStatus } from '../../../models/order/order-status';
import { PaymentStatus } from '../../../models/order/payment-status';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user/user.model';
import { CommentService } from '../../../services/comment.service';

@Component({
    standalone: true,
    selector: 'app-stats-widget',
    imports: [CommonModule],
    template: `<div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">Commandes</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ totalOrders() }}</div>
                    </div>
                    <div class="flex items-center justify-center bg-blue-100 dark:bg-blue-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-shopping-cart text-blue-500 !text-xl"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">{{ totalPendingOrders() }}</span>
                <span class="text-muted-color"> commandes en attente</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">Revenu</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ totalRevenue() | currency: 'MAD ' }}</div>
                    </div>
                    <div class="flex items-center justify-center bg-orange-100 dark:bg-orange-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-dollar text-orange-500 !text-xl"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">%{{ lastWeekRevenuePercentage() || 0 }}+ </span>
                <span class="text-muted-color">depuis la semaine dernière</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">Clients</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ totalUsers() }}</div>
                    </div>
                    <div class="flex items-center justify-center bg-cyan-100 dark:bg-cyan-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-users text-cyan-500 !text-xl"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">+{{ totalNewUsersLastMonth() }} </span>
                <span class="text-muted-color">depuis le mois dernier</span>
            </div>
        </div>
        <div class="col-span-12 lg:col-span-6 xl:col-span-3">
            <div class="card mb-0">
                <div class="flex justify-between mb-4">
                    <div>
                        <span class="block text-muted-color font-medium mb-4">Commentaires</span>
                        <div class="text-surface-900 dark:text-surface-0 font-medium text-xl">{{ totalComments() }}</div>
                    </div>
                    <div class="flex items-center justify-center bg-purple-100 dark:bg-purple-400/10 rounded-border" style="width: 2.5rem; height: 2.5rem">
                        <i class="pi pi-comment text-purple-500 !text-xl"></i>
                    </div>
                </div>
                <span class="text-primary font-medium">{{ totalCommentsLastWeek() }} </span>
                <span class="text-muted-color">depuis la semaine derniere</span>
            </div>
        </div>`
})
export class StatsWidget implements OnInit {
    orderService = inject(OrderService);

    totalOrders = signal<number>(0);
    totalPendingOrders = signal<number>(0);
    totalRevenue = signal<number>(0);
    lastWeekRevenuePercentage = signal<number>(0);

    userService = inject(UserService);
    totalUsers = signal<number>(0);
    totalNewUsersLastMonth = signal<number>(0);

    commentService = inject(CommentService);
    totalComments = signal<number>(0);
    totalCommentsLastWeek = signal<number>(0);

    ngOnInit() {
        this.orderService.getOrders().subscribe({
            next: (orders) => {
                this.totalOrders.set(orders.length);
                this.totalPendingOrders.set(orders.filter((order) => order.status === OrderStatus.PENDING).length);
                const deleveredOrders = orders.filter((order) => order.status === OrderStatus.DELIVERED || order.invoice.paymentMethod.status === PaymentStatus.PAID);
                this.totalRevenue.set(deleveredOrders.reduce((total, order) => total + order.invoice.totalPrice, 0));
                const lastWeekRevenu = deleveredOrders.filter((order) => new Date(order.createdAt!) > new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)).reduce((total, order) => total + order.invoice.totalPrice, 0);
                const totalRevenueValue = this.totalRevenue();
                this.lastWeekRevenuePercentage.set(totalRevenueValue > 0 ? (lastWeekRevenu / totalRevenueValue) * 100 : 0);
            },
            error: (error) => console.error(error) //TODO: handle error
        });

        this.userService.getUsers().subscribe({
            next: (users) => {
                this.totalUsers.set(users.length);
                this.totalNewUsersLastMonth.set(users.filter((user) => new Date(user.createdAt!) > new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)).length);
            },
            error: (error) => console.error(error) //TODO: handle error
        });

        this.commentService.getComments().subscribe({
            next: (comments) => {
                this.totalComments.set(comments.length);
                this.totalCommentsLastWeek.set(comments.filter((comment) => new Date(comment.createdAt!) > new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)).length);
            },
            error: (error) => console.error(error) //TODO: handle error
        });
    }
}
