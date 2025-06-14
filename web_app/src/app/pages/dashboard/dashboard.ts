import { Component } from '@angular/core';
import { StatsWidget } from './components/statswidget';
import { RecentSalesWidget } from './components/recentsaleswidget';
import { BestSellingWidget } from './components/bestsellingwidget';
import { ChartsComponent } from './components/charts/charts.component';

@Component({
    selector: 'app-dashboard',
    imports: [StatsWidget, RecentSalesWidget, BestSellingWidget, ChartsComponent],
    template: `
        <div class="grid grid-cols-12 gap-4">
            <h1 class="col-span-12 text-3xl font-bold text-surface-900 dark:text-surface-0 mb-1">Tableau de bord</h1>
            <app-stats-widget class="contents" />
            <div class="col-span-12">
                <app-revenue-stream-widget />
            </div>
            <div class="col-span-12">
                <app-recent-sales-widget />
                <app-best-selling-widget />
            </div>
        </div>
    `
})
export class Dashboard {}
