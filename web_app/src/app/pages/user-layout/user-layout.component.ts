import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterWidget } from './components/footerwidget';

@Component({
    selector: 'app-user-layout',
    imports: [RouterModule, NavbarComponent, FooterWidget],
    templateUrl: './user-layout.component.html'
})
export class UserLayoutComponent {}
