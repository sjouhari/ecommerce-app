import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { FloatLabelModule } from 'primeng/floatlabel';
import { AuthService } from '../../services/auth.service';
import { ContactService } from '../../services/contact.service';
import { Contact } from '../../models/contact/contact.model';

@Component({
    selector: 'app-contacts',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        ReactiveFormsModule,
        ButtonModule,
        RippleModule,
        ToastModule,
        ToolbarModule,
        RatingModule,
        InputTextModule,
        TextareaModule,
        SelectModule,
        RadioButtonModule,
        InputNumberModule,
        DialogModule,
        TagModule,
        InputIconModule,
        IconFieldModule,
        ConfirmDialogModule,
        FloatLabelModule
    ],
    templateUrl: 'contacts.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ContactsComponent implements OnInit {
    contactDialog: boolean = false;

    contactFormGroup!: FormGroup;

    contactService = inject(ContactService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    contacts = signal<Contact[]>([]);
    loading = signal(false);

    selectedContact!: Contact | null;
    selectedContacts!: Contact[] | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.init();
    }

    init() {
        this.initContactFormGroup();
        this.contactService.getContacts().subscribe({
            next: (contacts) => {
                this.contacts.set(contacts);
            }
        });
    }

    initContactFormGroup(contact?: Contact) {
        this.contactFormGroup = this.formBuilder.group({
            id: new FormControl(contact?.id || 0, [Validators.required]),
            response: new FormControl(contact?.response || '', [Validators.required])
        });
    }

    get formControls() {
        return this.contactFormGroup.controls;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    editContact(contact: Contact) {
        this.initContactFormGroup(contact);
        this.selectedContact = contact;
        this.contactDialog = true;
    }

    deleteSelectedContacts() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer toutes les contacts sélectionnées ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedContacts?.forEach((size) => this.onDeleteContact(size));
                this.selectedContacts = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les contacts sélectionnées ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.contactDialog = false;
        this.selectedContact = null;
        this.contactFormGroup.reset();
    }

    deleteContact(size: Contact) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer ce contact ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.onDeleteContact(size);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Le contact a été supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteContact(contact: Contact) {
        this.contactService.deleteContact(contact!.id!).subscribe({
            next: () => {
                this.contacts.update((subCategories) => subCategories.filter((val) => val.id !== contact!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    saveContact() {
        if (this.contactFormGroup.invalid) {
            this.contactFormGroup.markAllAsTouched();
            return;
        }

        this.loading.set(true);
        this.contactService.responseContact(this.contactFormGroup.value.id, this.contactFormGroup.value.response).subscribe({
            next: (size) => {
                this.init();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Contact Réponse',
                    detail: 'La réponse a bien été envoyée.',
                    life: 3000
                });
                this.contactDialog = false;
                this.selectedContact = null;
                this.loading.set(false);
                this.contactFormGroup.reset();
            },
            error: (error) => {
                console.log(error); //TODO: handle error
                this.loading.set(false);
            }
        });
    }
}
