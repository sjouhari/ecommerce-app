import { Component, inject, input, OnInit, signal, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
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
import { UserService } from '../../services/user.service';
import { User } from '../../models/user/user.model';
import { ActivatedRoute } from '@angular/router';
import { Profil } from '../../models/user/profil.model';
import { ProfilService } from '../../services/profil.service';
import { FloatLabelModule } from 'primeng/floatlabel';

@Component({
    selector: 'app-users',
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
    templateUrl: 'users.component.html',
    providers: [MessageService, ConfirmationService]
})
export class UsersComponent implements OnInit {
    role: string | null = null;
    userDialog: boolean = false;

    userFormGroup!: FormGroup;

    userService = inject(UserService);
    profilService = inject(ProfilService);
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    formBuilder = inject(FormBuilder);
    route = inject(ActivatedRoute);

    users = signal<User[]>([]);
    profiles = signal<Profil[]>([]);
    loading = signal(false);

    selectedUsers!: User[] | null;
    selectedUser!: User | null;

    @ViewChild('dt') dt!: Table;

    ngOnInit() {
        this.initUserFormGroup();
        this.getUsers();

        this.profilService.getProfils().subscribe({
            next: (profils) => {
                this.profiles.set(profils);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    initUserFormGroup(user?: User) {
        this.userFormGroup = this.formBuilder.group({
            id: new FormControl(user?.id || null),
            firstName: new FormControl(user?.firstName || '', [Validators.required]),
            lastName: new FormControl(user?.lastName || '', [Validators.required]),
            email: new FormControl(user?.email || '', [Validators.required, Validators.email]),
            enabled: new FormControl(user?.enabled || false),
            profilesIds: this.formBuilder.array(user?.profils?.map((p) => new FormControl(p.id)) || [])
        });
    }

    get profilesFormArray(): FormArray {
        return this.userFormGroup.get('profilesIds') as FormArray;
    }

    isChecked(id: number): boolean {
        return this.profilesFormArray.controls.some((control) => control.value === id);
    }

    onCheckboxChange(event: any) {
        const profilesIds = this.profilesFormArray;
        const value = +event.target.value;
        if (event.target.checked) {
            profilesIds.push(new FormControl(value));
        } else {
            const index = profilesIds.controls.findIndex((ctrl) => ctrl.value === value);
            if (index !== -1) {
                profilesIds.removeAt(index);
            }
        }
    }

    getUsers() {
        this.userService.getUsers().subscribe({
            next: (users) => {
                this.route.data.subscribe((data) => {
                    this.role = data['role'];
                });
                if (this.role === 'admin') {
                    const admins = users.filter((user) => user.profils?.map((profil) => profil.name).includes('ROLE_ADMIN'));
                    this.users.set(admins);
                } else if (this.role === 'vendor') {
                    const vendors = users.filter((user) => user.profils?.map((profil) => profil.name).includes('ROLE_SELLER') && !user.profils.map((profil) => profil.name).includes('ROLE_ADMIN'));
                    this.users.set(vendors);
                } else if (this.role === 'client') {
                    this.users.set(users.filter((user) => !user.profils?.map((profil) => profil.name).includes('ROLE_ADMIN') && !user.profils?.map((profil) => profil.name).includes('ROLE_SELLER')));
                } else {
                    this.users.set(users);
                }
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    get formControls() {
        return this.userFormGroup.controls;
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.initUserFormGroup();
        this.userDialog = true;
    }

    editUser(user: User) {
        this.initUserFormGroup(user);
        this.userDialog = true;
        this.selectedUser = user;
    }

    deleteSelectedUsers() {
        this.confirmationService.confirm({
            message: 'Êtes-vous subur de vouloir supprimer les utilisateurs sélectionnées ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.selectedUsers?.forEach((feature) => {
                    this.onDeleteUser(feature);
                });
                this.selectedUsers = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Toutes les utilisateurs sélectionnées ont été supprimées avec succès.',
                    life: 3000
                });
            }
        });
    }

    hideDialog() {
        this.userDialog = false;
        this.userFormGroup.reset();
    }

    deleteUser(user: User) {
        this.confirmationService.confirm({
            message: "Êtes-vous sûr de vouloir supprimer l'utilisateur " + user.firstName + ' ' + user.lastName + ' ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                console.log(user);
                this.onDeleteUser(user);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Suppression',
                    detail: 'Utilisateur supprimée avec succès.',
                    life: 3000
                });
            }
        });
    }

    onDeleteUser(user: User) {
        this.userService.deleteUser(user!.id!).subscribe({
            next: () => {
                this.users.update((users) => users.filter((val) => val.id !== user!.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    saveUser() {
        if (this.userFormGroup.invalid) {
            this.userFormGroup.markAllAsTouched();
            return;
        }

        if (this.userFormGroup.value.id) {
            this.userService.updateUser(this.userFormGroup.value.id, this.userFormGroup.value).subscribe({
                next: (user) => {
                    this.userDialog = false;
                    this.userFormGroup.reset();
                    this.selectedUser = null;
                    this.getUsers();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: "L'utilisateur a été modifiée avec succès.",
                        life: 3000
                    });
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        } else {
            this.userService.createUser(this.userFormGroup.value).subscribe({
                next: (user) => {
                    this.userDialog = false;
                    this.userFormGroup.reset();
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: "L'utilisateur a été créée avec succès.",
                        life: 3000
                    });
                    this.users.update((users) => [...users, user]);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
