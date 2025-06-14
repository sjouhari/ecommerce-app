import { Component, inject } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { OrderService } from '../../../services/order.service';

@Component({
    standalone: true,
    selector: 'app-revenue-stream-widget',
    imports: [ChartModule],
    template: `<div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
        <div class="card bg-white h-full">
            <h3 class="text-lg font-semibold text-center mb-2">Les produits les mieux vendus</h3>
            <p-chart type="bar" [data]="bestSellingChartData" [options]="{ responsive: true, plugins: { legend: { position: 'top', labels: { boxWidth: 40 } } }, barThickness: 30 }"></p-chart>
        </div>
        <div class="card bg-white h-full">
            <h3 class="text-lg font-semibold text-center mb-2">Les vendeurs les mieux vendus</h3>
            <p-chart type="bar" [data]="topSellersChartData" [options]="{ responsive: true, plugins: { legend: { position: 'top', labels: { boxWidth: 40 } } }, barThickness: 30 }"></p-chart>
        </div>
    </div>`
})
export class RevenueStreamWidget {
    orderService = inject(OrderService);
    bestSellingChartData: any;
    topSellersChartData: any;

    ngOnInit() {
        this.orderService.getBestSellingProducts().subscribe((data) => {
            this.bestSellingChartData = {
                labels: data.map((p) => 'Produit ' + p.productId).slice(0, 10),
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
                labels: data.map((s) => 'Vendeur ' + s.storeId).slice(0, 10),
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
