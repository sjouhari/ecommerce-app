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
import { Profil } from '../../models/user/profil.model';
import { ProfilService } from '../../services/profil.service';
import { PickListModule } from 'primeng/picklist';
import { FeatureService } from '../../services/feature.service';
import { Feature } from '../../models/user/feature.model';
import { FloatLabelModule } from 'primeng/floatlabel';

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
        PickListModule,
        FloatLabelModule
    ],
    templateUrl: 'profiles.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ProfilesComponent implements OnInit {
    profilDialog: boolean = false;

    profilFormGroup!: FormGroup;

    profilService = inject(ProfilService);
    featureService = inject(FeatureService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    profils = signal<Profil[]>([]);
    loading = signal(false);

    @ViewChild('dt') dt!: Table;

    sourceFeatures: any[] = [];

    ngOnInit() {
        this.initProfilFormGroup();
        this.profilService.getProfils().subscribe({
            next: (profils) => {
                this.profils.set(profils);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
        this.featureService.getFeatures().subscribe({
            next: (features) => {
                this.sourceFeatures = features;
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    get formControls() {
        return this.profilFormGroup.controls;
    }

    initProfilFormGroup(profil?: Profil) {
        this.profilFormGroup = this.formBuilder.group({
            id: new FormControl(profil?.id || null),
            name: new FormControl(profil?.name || '', [Validators.required])
        });
    }

    openNew() {
        this.initProfilFormGroup();
        this.profilDialog = true;
    }

    editProfil(profil: Profil) {
        this.initProfilFormGroup(profil);
        this.profilDialog = true;
    }

    hideDialog() {
        this.profilDialog = false;
        this.profilFormGroup.reset();
    }

    deleteProfil(profil: Profil) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sur de vouloir supprimer le profil ' + profil.name + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.profilService.deleteProfil(profil.id).subscribe({
                    next: () => {
                        this.profils.update((profils) => profils.filter((p) => p.id !== profil.id));
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Suppression',
                            detail: 'Profile supprimé avec succès.',
                            life: 3000
                        });
                    },
                    error: (error) => {
                        console.log(error); //TODO: handle error
                    }
                });
            }
        });
    }

    saveProfil() {
        if (this.profilFormGroup.invalid) {
            this.profilFormGroup.markAllAsTouched();
            return;
        }
        this.loading.set(true);
        if (this.profilFormGroup.value.id === null) {
            this.profilService.createProfil(this.profilFormGroup.value).subscribe({
                next: (profil) => {
                    this.profils.update((profils) => [...profils, profil]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Le profil a été création avec succès.',
                        life: 3000
                    });
                    this.profilDialog = false;
                    this.loading.set(false);
                    this.profilFormGroup.reset();
                },
                error: (error) => {
                    this.loading.set(false);
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.profilService.updateProfil(this.profilFormGroup.value).subscribe({
                next: (profil) => {
                    this.profils.update((profils) => profils.map((p) => (p.id === profil.id ? profil : p)));
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'Le profil a été modifié avec succès.',
                        life: 3000
                    });
                    this.profilDialog = false;
                    this.loading.set(false);
                    this.profilFormGroup.reset();
                },
                error: (error) => {
                    this.loading.set(false);
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
