import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { StoreService } from '../../services/store.service';
import { Store } from '../../models/user/store.model';

@Component({
    selector: 'app-categories',
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
    templateUrl: 'stores.component.html',
    providers: [MessageService, ConfirmationService]
})
export class StoresComponent implements OnInit {
    storeService = inject(StoreService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);

    stores = signal<Store[]>([]);

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.getStores();
    }

    private getStores() {
        this.storeService.getAllStores().subscribe({
            next: (stores) => {
                this.stores.set(stores);
            },
            error: (error) => {
                console.log(error);
            }
        });
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    approveStore(store: Store) {
        this.storeService.approveStore(store.id).subscribe({
            next: () => {
                this.getStores();
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Le magasin a bien été approuvé' });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    rejectStore(store: Store) {
        this.storeService.rejectStore(store.id).subscribe({
            next: () => {
                this.getStores();
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Le magasin a bien été rejeté' });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
