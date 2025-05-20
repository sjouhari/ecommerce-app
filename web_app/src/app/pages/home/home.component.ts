import { Component } from '@angular/core';
import { FooterWidget } from './components/footerwidget';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
    selector: 'app-home',
    imports: [FooterWidget, RouterModule, NavbarComponent],
    templateUrl: './home.component.html'
})
export class HomeComponent {}
