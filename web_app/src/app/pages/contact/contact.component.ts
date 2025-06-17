import { Contact } from './../../models/contact/contact.model';
import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { ContactService } from '../../services/contact.service';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { first } from 'rxjs';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-contact',
    imports: [ReactiveFormsModule, InputTextModule, Textarea, ButtonModule, ToastModule],
    templateUrl: './contact.component.html',
    providers: [MessageService]
})
export class ContactComponent implements OnInit {
    contactService = inject(ContactService);
    authService = inject(AuthService);
    formBuilder = inject(FormBuilder);
    messageService = inject(MessageService);

    loading = false;

    contactFormGroup!: FormGroup;

    ngOnInit(): void {
        this.contactFormGroup = this.formBuilder.group({
            firstName: new FormControl('', [Validators.required]),
            lastName: new FormControl('', [Validators.required]),
            phone: new FormControl('', [Validators.required]),
            email: new FormControl('', [Validators.required, Validators.email]),
            subject: new FormControl('', [Validators.required]),
            message: new FormControl('', [Validators.required])
        });
    }

    get formControls() {
        return this.contactFormGroup.controls;
    }

    sendMessage() {
        if (this.contactFormGroup.invalid) {
            this.contactFormGroup.markAllAsTouched();
            return;
        }

        this.loading = true;

        const contact: Contact = {
            name: this.contactFormGroup.value.firstName + ' ' + this.contactFormGroup.value.lastName,
            email: this.contactFormGroup.value.email,
            phone: this.contactFormGroup.value.phone,
            subject: this.contactFormGroup.value.subject,
            message: this.contactFormGroup.value.message,
            userId: this.authService.currentUser()?.id!
        };
        this.contactService.sendMessage(contact).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Message envoyé',
                    detail: 'Votre message a bien été envoyé.'
                });
                this.contactFormGroup.reset();
                this.loading = false;
            },
            error: (error) => {
                this.loading = false;
                console.log(error.error); //TODO: handle error
            }
        });
    }
}
