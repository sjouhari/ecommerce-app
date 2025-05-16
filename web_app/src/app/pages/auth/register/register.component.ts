import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-register',
    imports: [ReactiveFormsModule, FormsModule, FloatLabelModule, InputTextModule, ButtonModule, RouterLink],
    templateUrl: './register.component.html'
})
export class RegisterComponent {
    registerFormGroup!: FormGroup;
    authService = inject(AuthService);
    errorMessage = signal('');
    successMessage = signal('');
    loading = signal(false);

    constructor(private formBuilder: FormBuilder) {}

    ngOnInit(): void {
        this.registerFormGroup = this.formBuilder.group({
            firstName: new FormControl('', [Validators.required]),
            lastName: new FormControl('', [Validators.required]),
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', [Validators.required, Validators.minLength(8)])
        });
    }

    get formControls() {
        return this.registerFormGroup.controls;
    }

    register() {
        this.errorMessage.set('');
        this.successMessage.set('');
        if (this.registerFormGroup.invalid) {
            this.registerFormGroup.markAllAsTouched();
            return;
        }
        this.loading.set(true);
        this.authService.register(this.registerFormGroup.value).subscribe({
            next: (response) => {
                this.loading.set(false);
                this.successMessage.set(response.message);
                this.registerFormGroup.reset();
            },
            error: (error) => {
                this.loading.set(false);
                this.errorMessage.set(error.error.message);
            }
        });
    }
}
