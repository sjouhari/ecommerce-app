import { Component, inject, OnInit, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { Address } from '../../models/order/address.model';
import { AuthService } from '../../services/auth.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AddressService } from '../../services/address.service';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DialogModule } from 'primeng/dialog';
import { PasswordModule } from 'primeng/password';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order/order.model';
import { ToastModule } from 'primeng/toast';
import { UserService } from '../../services/user.service';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Store } from '../../models/user/store.model';
import { StoreService } from '../../services/store.service';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    imports: [CommonModule, ReactiveFormsModule, ToastModule, DialogModule, TableModule, PanelModule, CardModule, ButtonModule, InputTextModule, PasswordModule, TextareaModule, FloatLabelModule, ConfirmDialogModule, RouterLink],
    providers: [MessageService, ConfirmationService]
})
export class UserProfileComponent implements OnInit {
    userInfosFormGroup!: FormGroup;
    changePasswordFormGroup!: FormGroup;
    addressFormGroup!: FormGroup;
    storeFormGroup!: FormGroup;

    saveBtnLoading = signal(false);
    changePasswordBtnLoading = signal(false);
    addressBtnLoading = signal(false);
    storeBtnLoading = signal(false);

    formBuilder = inject(FormBuilder);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    addressService = inject(AddressService);
    orderService = inject(OrderService);
    userService = inject(UserService);
    storeService = inject(StoreService);
    confirmationService = inject(ConfirmationService);

    addressDialog = signal(false);
    storeDialog = signal(false);

    addresses = signal<Address[]>([]);
    orders = signal<Order[]>([]);

    currentUser = signal(this.authService.currentUser());

    ngOnInit(): void {
        this.userInfosFormGroup = this.formBuilder.group({
            firstName: new FormControl(this.currentUser()?.firstName, [Validators.required]),
            lastName: new FormControl(this.currentUser()?.lastName, [Validators.required]),
            email: new FormControl(this.currentUser()?.email)
        });

        this.changePasswordFormGroup = this.formBuilder.group({
            currentPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
            newPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
            confirmNewPassword: new FormControl('', [Validators.required, Validators.minLength(8)])
        });

        this.addressService.getUserAddresses(this.currentUser()?.id!).subscribe({
            next: (addresses) => {
                this.addresses.set(addresses);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });

        this.initAddressFormGroup();
        this.initStoreFormGroup();

        this.orderService.getUserOrders(this.currentUser()?.id!).subscribe({
            next: (orders) => {
                this.orders.set(orders);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    initAddressFormGroup(address?: Address) {
        this.addressFormGroup = this.formBuilder.group({
            id: new FormControl(address?.id || ''),
            userId: new FormControl(this.currentUser()?.id),
            firstName: new FormControl(address?.firstName || '', [Validators.required]),
            lastName: new FormControl(address?.lastName || '', [Validators.required]),
            phone: new FormControl(address?.phone || '', [Validators.required]),
            deliveryAddress: new FormControl(address?.deliveryAddress || '', [Validators.required]),
            country: new FormControl(address?.country || '', [Validators.required]),
            city: new FormControl(address?.city || '', [Validators.required]),
            postalCode: new FormControl(address?.postalCode || '', [Validators.required])
        });
    }

    initStoreFormGroup() {
        this.storeFormGroup = this.formBuilder.group({
            id: new FormControl(this.currentUser()?.store?.id || ''),
            userId: new FormControl(this.currentUser()?.id),
            name: new FormControl(this.currentUser()?.store?.name || '', [Validators.required]),
            email: new FormControl(this.currentUser()?.store?.email || '', [Validators.required, Validators.email]),
            phoneNumber: new FormControl(this.currentUser()?.store?.phoneNumber || '', [Validators.required]),
            address: new FormControl(this.currentUser()?.store?.address || '', [Validators.required])
        });
    }

    get userFormControls() {
        return this.userInfosFormGroup.controls;
    }

    get passwdFormControls() {
        return this.changePasswordFormGroup.controls;
    }

    get addressFormControls() {
        return this.addressFormGroup.controls;
    }

    get storeFormControls() {
        return this.storeFormGroup.controls;
    }

    getFullName() {
        return `${this.currentUser()?.firstName} ${this.currentUser()?.lastName}`;
    }

    logout() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir vous deconnecter ?',
            header: 'Déconnexion',
            icon: 'pi pi-exclamation-triangle',
            acceptLabel: 'Déconnexion',
            rejectLabel: 'Annuler',
            accept: () => {
                this.authService.logout();
            }
        });
    }

    confirmDeleteAddress(address: Address) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer cette adresse ?',
            header: 'Suppression',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {}
        });
    }

    saveChanges() {
        if (this.userInfosFormGroup.invalid) {
            this.userInfosFormGroup.markAllAsTouched();
            return;
        }

        this.saveBtnLoading.set(true);
        this.userService.updateUser(this.currentUser()?.id!, this.userInfosFormGroup.value).subscribe({
            next: (response) => {
                this.saveBtnLoading.set(false);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Modification',
                    detail: 'Informations modifiées avec succès'
                });
            },
            error: (error) => {
                this.saveBtnLoading.set(false);
                this.messageService.add({
                    severity: 'error',
                    summary: 'Erreur',
                    detail: error?.error?.message || 'Une erreur est survenue lors de la modification des informations'
                });
            }
        });
    }

    changePassword() {
        if (this.changePasswordFormGroup.invalid) {
            this.changePasswordFormGroup.markAllAsTouched();
            return;
        }

        this.changePasswordBtnLoading.set(true);
        this.userService.changePassword(this.currentUser()?.id!, this.changePasswordFormGroup.value.currentPassword!, this.changePasswordFormGroup.value.newPassword!).subscribe({
            next: (response) => {
                this.changePasswordBtnLoading.set(false);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Modification',
                    detail: 'Mot de passe modifié avec succès'
                });
            },
            error: (error) => {
                this.changePasswordBtnLoading.set(false);
                this.messageService.add({
                    severity: 'error',
                    summary: 'Erreur',
                    detail: error?.error?.message || 'Une erreur est survenue lors de la modification du mot de passe'
                });
            }
        });
    }

    hideAddressDialog() {
        this.addressDialog.set(false);
    }

    hideStoreDialog() {
        this.storeDialog.set(false);
    }

    showAddressDialog(address?: Address) {
        this.initAddressFormGroup(address);
        this.addressDialog.set(true);
    }

    showStoreDialog(store?: Store) {
        this.storeDialog.set(true);
    }

    saveAddress() {
        if (this.addressFormGroup.invalid) {
            this.addressFormGroup.markAllAsTouched();
            return;
        }
        const address = this.addressFormGroup.value;
        this.addressBtnLoading.set(true);

        if (address.id) {
            this.addressService.updateAddress(address.id, address).subscribe({
                next: (address) => {
                    this.addresses.update((addressess) => addressess.map((add) => (add.id === address.id ? address : add)));
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'Adresse modifiée avec succès'
                    });
                    this.addressBtnLoading.set(false);
                    this.hideAddressDialog();
                },
                error: (error) => {
                    this.addressBtnLoading.set(false);
                    console.log(error);
                }
            });
        } else {
            this.addressService.createAddress(address).subscribe({
                next: (address) => {
                    this.addresses.update((addressess) => [...addressess, address]);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Adresse créée avec succès'
                    });
                    this.addressBtnLoading.set(false);
                    this.hideAddressDialog();
                },
                error: (error) => {
                    this.addressBtnLoading.set(false);
                    console.log(error);
                }
            });
        }
    }

    saveStore() {
        if (this.storeFormGroup.invalid) {
            this.storeFormGroup.markAllAsTouched();
            return;
        }
        const store = this.storeFormGroup.value;
        this.storeBtnLoading.set(true);

        if (store.id) {
            this.storeService.updateStore(store).subscribe({
                next: (store) => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Modification',
                        detail: 'Magasin modifié avec succès'
                    });
                    this.storeBtnLoading.set(false);
                    this.authService.getCurrentUser().subscribe({
                        next: (user) => {
                            this.authService.setCurrentUser(user);
                            this.currentUser.set(user);
                        },
                        error: (error) => {
                            this.authService.setCurrentUser(null);
                            this.currentUser.set(null);
                            console.log(error); //TODO: handle error
                        }
                    });
                    this.hideStoreDialog();
                },
                error: (error) => {
                    this.storeBtnLoading.set(false);
                    console.log(error);
                }
            });
        } else {
            this.storeService.createStore(store).subscribe({
                next: (address) => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Votre mafasin a été créé avec succès'
                    });
                    this.storeBtnLoading.set(false);
                    this.authService.getCurrentUser().subscribe({
                        next: (user) => {
                            this.authService.setCurrentUser(user);
                            this.currentUser.set(user);
                        },
                        error: (error) => {
                            this.authService.setCurrentUser(null);
                            this.currentUser.set(null);
                            console.log(error); //TODO: handle error
                        }
                    });
                    this.hideStoreDialog();
                },
                error: (error) => {
                    this.storeBtnLoading.set(false);
                    console.log(error);
                }
            });
        }
    }

    confirmDeleteStore() {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer votre magasin ?',
            header: 'Suppression',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {}
        });
    }
}
