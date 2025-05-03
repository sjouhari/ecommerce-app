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
import { Profil } from '../../models/user/role.model';
import { ProfilService } from '../../services/profil.service';
import { PickListModule } from 'primeng/picklist';

@Component({
    selector: 'app-profils',
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
        PickListModule
    ],
    templateUrl: 'profils.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ProfilsComponent implements OnInit {
    profilDialog: boolean = false;

    profilFormGroup!: FormGroup;

    profilService = inject(ProfilService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    profils = signal<Profil[]>([]);

    profil!: Profil;

    selectedProfils!: Profil[] | null;

    submitted: boolean = false;

    @ViewChild('dt') dt!: Table;

    sourceCities: any[] = [];

    targetCities: any[] = [];

    ngOnInit() {
        this.initProfilFormGroup();
        this.profilService.getProfils().subscribe({
            next: (profils) => {
                this.profils.set(profils);
            }
        });
    }

    initProfilFormGroup(profil?: Profil) {
        this.profilFormGroup = this.formBuilder.group({
            name: new FormControl(profil?.name || '', [Validators.required])
        });
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.profil = { id: 0, name: '' };
        this.submitted = false;
        this.profilDialog = true;
    }

    editProfil(profil: Profil) {
        this.initProfilFormGroup(profil);
        this.profilDialog = true;
    }

    deleteSelectedProfils() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedProfils = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Products Deleted',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.profilDialog = false;
        this.submitted = false;
    }

    deleteProfil(profil: Profil) {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete ' + profil.name + '?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.profil = { id: 0, name: '' };
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Deleted',
                    life: 3000
                });
            }
        });
    }

    saveProfil() {
        this.profilService.createProfil(this.profilFormGroup.value).subscribe({
            next: (profil) => {
                this.profils.update((profils) => [...profils, profil]);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Created',
                    life: 3000
                });
                this.profilDialog = false;
                this.profilFormGroup.reset();
            }
        });
    }
}
