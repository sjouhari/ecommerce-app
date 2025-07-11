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
import { CommentService } from '../../services/comment.service';
import { CommentModel } from '../../models/product/comment.model';

@Component({
    selector: 'app-comments',
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
    templateUrl: './comments.component.html',
    providers: [MessageService, ConfirmationService]
})
export class CommentsComponent implements OnInit {
    commentDialog: boolean = false;

    commentFormGroup!: FormGroup;

    commentService = inject(CommentService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);

    comments = signal<CommentModel[]>([]);
    loading = signal(false);

    selectedComments!: CommentModel[] | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.getComments();
    }

    getComments() {
        this.commentService.getComments().subscribe({
            next: (comments) => {
                this.comments.set(comments);
            }
        });
    }

    get formControls() {
        return this.commentFormGroup.controls;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    deleteSelectedComments() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer toutes les commentaires sélectionnées ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedComments?.forEach((size) => this.onDeleteComment(size));
                this.selectedComments = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les commentaires sélectionnées ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.commentDialog = false;
        this.commentFormGroup.reset();
    }

    deleteComment(comment: CommentModel) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer ce commentaire ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.onDeleteComment(comment);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Le commentaire a été supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteComment(comment: CommentModel) {
        this.commentService.deleteComment(comment!.id!).subscribe({
            next: () => {
                this.comments.update((subCategories) => subCategories.filter((val) => val.id !== comment!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    approveComment(comment: CommentModel) {
        this.commentService.approveComment(comment!.id!).subscribe({
            next: () => {
                this.getComments();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Approuver',
                    detail: 'Le commentaire a été approuvé avec succès.',
                    life: 3000
                });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    rejectComment(comment: CommentModel) {
        this.commentService.rejectComment(comment!.id!).subscribe({
            next: () => {
                this.getComments();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Rejeté',
                    detail: 'Le commentaire a été refusé avec succès.',
                    life: 3000
                });
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
