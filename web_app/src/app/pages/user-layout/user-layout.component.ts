import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
    selector: 'app-user-layout',
    imports: [RouterModule, NavbarComponent],
    templateUrl: './user-layout.component.html'
})
export class UserLayoutComponent {}
