import { Component } from '@angular/core';
import { TopbarWidget } from './components/topbarwidget.component';
import { FooterWidget } from './components/footerwidget';
import { HeroWidget } from './components/herowidget';

@Component({
    selector: 'app-home',
    imports: [TopbarWidget, FooterWidget, HeroWidget],
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss'
})
export class HomeComponent {}
