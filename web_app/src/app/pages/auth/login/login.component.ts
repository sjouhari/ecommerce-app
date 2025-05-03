import { Component, inject, OnInit, signal } from '@angular/core';
import { FormGroup, FormBuilder, ReactiveFormsModule, FormsModule, FormControl, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-login',
    imports: [ReactiveFormsModule, FormsModule, FloatLabelModule, InputTextModule, ButtonModule, RouterLink],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
    loginFormGroup!: FormGroup;
    authService = inject(AuthService);
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
            return;
        }
        this.loading.set(true);
        this.authService.login(this.loginFormGroup.value).subscribe({
            next: (response) => {
                this.authService.setToken(response.token);
                this.loading.set(false);
                this.router.navigate(['/']);
            },
            error: (error) => {
                this.loading.set(false);
                this.errorMessage.set(error.error.message);
            }
        });
    }
}
