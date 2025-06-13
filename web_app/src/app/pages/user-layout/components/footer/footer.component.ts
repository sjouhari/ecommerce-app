import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { UserService } from '../../../../services/user.service';
import { AuthService } from '../../../../services/auth.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
    selector: 'footer-widget',
    standalone: true,
    imports: [RouterModule, FormsModule, InputTextModule, ButtonModule, ToastModule],
    providers: [MessageService],
    templateUrl: './footer.component.html'
})
export class FooterComponent {
    email: string = '';
    currentYear = new Date().getFullYear();

    router = inject(Router);
    userService = inject(UserService);
    authService = inject(AuthService);
    messageService = inject(MessageService);

    subscribe() {
        if (!this.email || !this.email.includes('@')) {
            this.messageService.add({ severity: 'error', summary: 'Adresse email invalide', detail: 'Veuillez entrer une adresse email valide.' });
            return;
        }

        this.userService.subscribeToNewsletter(this.authService.currentUser()?.id!, this.email).subscribe({
            next: () => {
                this.messageService.add({ severity: 'success', summary: 'Abonnement reussi', detail: 'Merci pour votre abonnement, ' + this.email + ' !' });
                this.email = '';
                this.authService.getCurrentUser().subscribe({
                    next: (user) => {
                        this.authService.setCurrentUser(user);
                    },
                    error: (error) => {
                        console.log(error); //TODO: handle error
                    }
                });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
