import { Component, inject, OnInit, signal } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  ReactiveFormsModule,
  FormsModule,
  FormControl,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    FloatLabelModule,
    InputTextModule,
    ButtonModule,
    RouterLink,
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css',
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordFormGroup!: FormGroup;
  authService = inject(AuthService);

  errorMessage = signal('');
  successMessage = signal('');
  loading = signal(false);

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.forgotPasswordFormGroup = this.formBuilder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
    });
  }

  get formControls() {
    return this.forgotPasswordFormGroup.controls;
  }

  forgotPassword() {
    this.errorMessage.set('');
    this.successMessage.set('');
    if (this.forgotPasswordFormGroup.invalid) {
      return;
    }

    this.loading.set(true);
    this.authService
      .forgotPassword(this.forgotPasswordFormGroup.value)
      .subscribe({
        next: (response) => {
          this.loading.set(false);
          this.successMessage.set(response.message);
        },
        error: (error) => {
          this.loading.set(false);
          this.errorMessage.set(error?.error?.message);
        },
      });
  }
}
