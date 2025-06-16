import { Component, inject } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { OrderService } from '../../../../services/order.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
    standalone: true,
    selector: 'app-revenue-stream-widget',
    imports: [ChartModule],
    templateUrl: './charts.component.html'
})
export class ChartsComponent {
    orderService = inject(OrderService);
    authService = inject(AuthService);

    bestSellingChartData: any;
    topSellersChartData: any;

    bestSellingChartOptions = {
        responsive: true,
        scales: {
            x: {
                display: false
            },
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 1
                }
            }
        },
        plugins: {
            legend: { position: 'top', labels: { boxWidth: 40, font: { size: 12 }, hover: { font: { size: 14 } } } },
            title: {
                display: true,
                text: 'Les produits les mieux vendus',
                font: {
                    size: 15
                }
            },
            labels: {
                display: false
            }
        },
        barThickness: 40
    };

    topSellersChartOptions = {
        responsive: true,
        plugins: {
            legend: { position: 'top', labels: { boxWidth: 40, font: { size: 12 } } },
            title: {
                display: true,
                text: 'Les meilleures vendeurs',
                font: {
                    size: 15
                }
            },
            labels: {
                display: false
            }
        },
        barThickness: 40
    };

    ngOnInit() {
        if (this.authService.isAdmin()) {
            this.orderService.getBestSellingProducts().subscribe((data) => {
                this.bestSellingChartData = {
                    labels: data.map((p) => 'Produit ' + p.productName).slice(0, 10),
                    datasets: [
                        {
                            label: 'Quantité vendue',
                            backgroundColor: '#42A5F5',
                            data: data.map((p) => p.totalSold)
                        }
                    ]
                };
            });
        } else {
            this.orderService.getBestSellingProductsByStoreId(this.authService.currentUser()?.store?.id!).subscribe((data) => {
                this.bestSellingChartData = {
                    labels: data.map((p) => 'Produit ' + p.productName).slice(0, 10),
                    datasets: [
                        {
                            label: 'Quantité vendue',
                            backgroundColor: '#42A5F5',
                            data: data.map((p) => p.totalSold)
                        }
                    ]
                };
            });
        }

        this.orderService.getTopSellingStores().subscribe({
            next: (data) => {
                this.topSellersChartData = {
                    labels: data.map((s) => s.storeName).slice(0, 10),
                    datasets: [
                        {
                            label: 'Produits vendus',
                            backgroundColor: '#66BB6A',
                            data: data.map((s) => s.totalSold)
                        }
                    ]
                };
            }
        });
    }
}
