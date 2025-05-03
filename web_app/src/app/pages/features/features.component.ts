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
import { Feature } from '../../models/user/feature.model';
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

    selectedFeatures!: Feature[] | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.initFeatureFormGroup();
        this.featureService.getFeatures().subscribe({
            next: (features) => {
                this.features.set(features);
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
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedFeatures = null;
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
        this.featureDialog = false;
    }

    deleteFeature(size: Feature) {
        if (this.featureFormGroup.invalid) {
            return;
        }
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer la sous catégorie ' + size.libelle + '?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.featureService.deleteFeature(size!.id!).subscribe({
                    next: () => {
                        this.features.update((subCategories) => subCategories.filter((val) => val.id !== size!.id));
                    }
                });
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'SubCategory Deleted',
                    life: 3000
                });
            }
        });
    }

    saveFeature() {
        if (this.featureFormGroup.invalid) {
            return;
        }

        if (this.featureFormGroup.value.id) {
            this.featureService.updateFeature(this.featureFormGroup.value).subscribe({
                next: () => {
                    this.features.update((subCategories) =>
                        subCategories.map((subCategory) => {
                            if (subCategory.id === this.featureFormGroup.value.id) {
                                return { ...subCategory, ...this.featureFormGroup.value };
                            }
                            return subCategory;
                        })
                    );
                }
            });
        } else {
            this.featureService.createFeature(this.featureFormGroup.value).subscribe({
                next: () => {
                    this.features.update((subCategories) => [...subCategories, this.featureFormGroup.value]);
                }
            });
        }
    }
}
