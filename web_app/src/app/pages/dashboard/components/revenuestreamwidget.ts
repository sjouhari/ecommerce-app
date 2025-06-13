import { Component, inject } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { debounceTime, Subscription } from 'rxjs';
import { LayoutService } from '../../../layout/service/layout.service';
import { OrderService } from '../../../services/order.service';

@Component({
    standalone: true,
    selector: 'app-revenue-stream-widget',
    imports: [ChartModule],
    template: `<div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <p-chart type="bar" [data]="bestSellingChartData" [options]="{ responsive: true, plugins: { legend: { position: 'top' } } }"></p-chart>
        <p-chart type="bar" [data]="topSellersChartData" [options]="{ responsive: true, plugins: { legend: { position: 'top' } } }"></p-chart>
    </div> `
})
export class RevenueStreamWidget {
    orderService = inject(OrderService);
    bestSellingChartData: any;
    topSellersChartData: any;

    ngOnInit() {
        this.orderService.getBestSellingProducts().subscribe((data) => {
            this.bestSellingChartData = {
                labels: data.map((p) => 'Produit ' + p.productId),
                datasets: [
                    {
                        label: 'Quantité vendue',
                        backgroundColor: '#42A5F5',
                        data: data.map((p) => p.totalSold)
                    }
                ]
            };
        });

        this.orderService.getTopSellingStores().subscribe((data) => {
            this.topSellersChartData = {
                labels: data.map((s) => 'Vendeur ' + s.storeId),
                datasets: [
                    {
                        label: 'Produits vendus',
                        backgroundColor: '#66BB6A',
                        data: data.map((s) => s.totalSold)
                    }
                ]
            };
        });
    }
}
