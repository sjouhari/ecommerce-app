import { Component, inject, OnInit, signal } from '@angular/core';
import { FormGroup, FormBuilder, ReactiveFormsModule, FormsModule, FormControl, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';

@Component({
    selector: 'app-login',
    imports: [ReactiveFormsModule, FormsModule, FloatLabelModule, InputTextModule, ButtonModule, RouterLink],
    templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
    loginFormGroup!: FormGroup;
    authService = inject(AuthService);
    userService = inject(UserService);
    router = inject(Router);
    formBuilder = inject(FormBuilder);

    errorMessage = signal('');
    loading = signal(false);

    ngOnInit(): void {
        this.loginFormGroup = this.formBuilder.group({
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', [Validators.required, Validators.minLength(8)])
        });
    }

    get formControls() {
        return this.loginFormGroup.controls;
    }

    login() {
        this.errorMessage.set('');
        if (this.loginFormGroup.invalid) {
            this.loginFormGroup.markAllAsTouched();
            return;
        }
        this.loading.set(true);
        this.authService.login(this.loginFormGroup.value).subscribe({
            next: (response) => {
                this.authService.setToken(response.token);
                this.authService.getCurrentUser().subscribe({
                    next: (user) => {
                        this.authService.setCurrentUser(user);
                        this.router.navigate(['/initialization']);
                        this.loading.set(false);
                    },
                    error: () => {
                        this.authService.setCurrentUser(null);
                    }
                });
            },
            error: (error) => {
                this.loading.set(false);
                this.errorMessage.set(error.error.message);
            }
        });
    }
}
