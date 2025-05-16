import { Component } from '@angular/core';
import { TopbarWidget } from './components/topbarwidget.component';
import { FooterWidget } from './components/footerwidget';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-home',
    imports: [TopbarWidget, FooterWidget, RouterModule],
    templateUrl: './home.component.html'
})
export class HomeComponent {}
