import { Feature } from './../../models/user/feature.model';
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
import { FeatureService } from '../../services/feature.service';

@Component({
    selector: 'app-features',
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
        ConfirmDialogModule
    ],
    templateUrl: 'features.component.html',
    providers: [MessageService, ConfirmationService]
})
export class FeaturesComponent implements OnInit {
    featureDialog: boolean = false;

    featureFormGroup!: FormGroup;

    featureService = inject(FeatureService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    features = signal<Feature[]>([]);
    loading = signal(false);

    selectedFeatures!: Feature[] | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.initFeatureFormGroup();
        this.featureService.getFeatures().subscribe({
            next: (features) => {
                this.features.set(features);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    initFeatureFormGroup(feature?: Feature) {
        this.featureFormGroup = this.formBuilder.group({
            id: new FormControl(feature?.id || null),
            libelle: new FormControl(feature?.libelle || '', [Validators.required]),
            resourceName: new FormControl(feature?.resourceName || '', [Validators.required]),
            action: new FormControl(feature?.action || '', [Validators.required])
        });
    }

    get formControls() {
        return this.featureFormGroup.controls;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.initFeatureFormGroup();
        this.featureDialog = true;
    }

    editFeature(feature: Feature) {
        this.initFeatureFormGroup(feature);
        this.featureDialog = true;
    }

    deleteSelectedFeatures() {
        this.confirmationService.confirm({
            message: 'Êtes-vous subur de vouloir supprimer les fonctionnalités sélectionnées ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedFeatures?.forEach((feature) => {
                    this.onDeleteFeature(feature);
                });
                this.selectedFeatures = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les fonctionnalités sélectionnées ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.featureDialog = false;
        this.featureFormGroup.reset();
    }

    deleteFeature(feature: Feature) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la fonctionnalité ' + feature.libelle + ' ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                console.log(feature);
                this.onDeleteFeature(feature);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Fonctionnalité supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteFeature(feature: Feature) {
        this.featureService.deleteFeature(feature!.id!).subscribe({
            next: () => {
                this.features.update((features) => features.filter((val) => val.id !== feature!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    saveFeature() {
        if (this.featureFormGroup.invalid) {
            return;
        }

        if (this.featureFormGroup.value.id) {
            this.featureService.updateFeature(this.featureFormGroup.value).subscribe({
                next: (feature) => {
                    this.features.update((features) => features.map((f) => (f.id === feature.id ? feature : f)));
                    this.featureDialog = false;
                    this.featureFormGroup.reset();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'La fonctionnalités a été modifiée avec succès.',
                        life: 3000
                    });
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.featureService.createFeature(this.featureFormGroup.value).subscribe({
                next: (feature) => {
                    this.featureDialog = false;
                    this.featureFormGroup.reset();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'La fonctionnalités a été créée avec succès.',
                        life: 3000
                    });
                    this.features.update((features) => [...features, feature]);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
