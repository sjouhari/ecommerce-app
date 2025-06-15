import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';

@Component({
    selector: 'app-user-layout',
    imports: [RouterModule, NavbarComponent, FooterComponent],
    templateUrl: './user-layout.component.html'
})
export class UserLayoutComponent {}
