import { Component } from '@angular/core';
import { TopbarWidget } from './components/topbarwidget.component';
import { FooterWidget } from './components/footerwidget';
import { HeroWidget } from './components/herowidget';
import { CategoriesWidget } from './components/categorieswidget';
import { NewProductsWidget } from './components/newproductswidget';

@Component({
    selector: 'app-home',
    imports: [TopbarWidget, FooterWidget, HeroWidget, CategoriesWidget, NewProductsWidget],
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss'
})
export class HomeComponent {}
