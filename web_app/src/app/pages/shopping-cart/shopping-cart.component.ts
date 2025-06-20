import { PaymentMethods } from '../../models/order/payment-methods';
import { Component, inject, input, OnInit, signal } from '@angular/core';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { InputNumber } from 'primeng/inputnumber';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { CheckboxModule } from 'primeng/checkbox';
import { Address } from '../../models/order/address.model';
import { AddressService } from '../../services/address.service';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { RadioButtonModule } from 'primeng/radiobutton';
import { OrderService } from '../../services/order.service';

@Component({
    selector: 'app-shopping-cart',
    imports: [CommonModule, InputNumber, FormsModule, ButtonModule, ToastModule, CheckboxModule, RadioButtonModule, DialogModule, ConfirmDialogModule, ReactiveFormsModule, FloatLabelModule, InputTextModule],
    templateUrl: './shopping-cart.component.html',
    providers: [MessageService, ConfirmationService]
})
export class ShoppingCartComponent implements OnInit {
    addressDialog: boolean = false;
    addressFormGroup!: FormGroup;
    formBuilder = inject(FormBuilder);
    loading = signal(false);
    checkout = signal(false);

    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);
    router = inject(Router);
    messageService = inject(MessageService);
    addressService = inject(AddressService);
    orderService = inject(OrderService);
    confirmationService = inject(ConfirmationService);

    selectedQuantity: number = 1;
    timeoutMap: { [key: number]: any } = {};
    totalPrice = signal<number>(0);

    addresses = signal<Address[]>([]);
    selectedAddress = signal<Address | null>(null);
    selectedPaymentMethod: PaymentMethods | null = null;

    paymentMethods = Object.entries(PaymentMethods).map(([key, value]) => ({ key, value }));
    chequeNumber = '';
    bankName = '';

    ngOnInit() {
        this.getShoppingCart();
        this.addressService.getUserAddresses(this.authService.currentUser()?.id!).subscribe({
            next: (addresses) => {
                this.addresses.set(addresses);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getShoppingCart() {
        this.shoppingCartService.getShoppingCart(this.authService.currentUser()?.id!).subscribe({
            next: (cart) => {
                this.shoppingCartService.setShoppingCart(cart);
                this.calculateTotalPrice();
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
        this.initAddressFormGroup();
    }

    initAddressFormGroup(address?: Address) {
        this.addressFormGroup = this.formBuilder.group({
            id: new FormControl(address?.id || ''),
            userId: new FormControl(this.authService.currentUser()?.id),
            firstName: new FormControl(address?.firstName || '', [Validators.required]),
            lastName: new FormControl(address?.lastName || '', [Validators.required]),
            phone: new FormControl(address?.phone || '', [Validators.required]),
            deliveryAddress: new FormControl(address?.deliveryAddress || '', [Validators.required]),
            country: new FormControl(address?.country || '', [Validators.required]),
            city: new FormControl(address?.city || '', [Validators.required]),
            postalCode: new FormControl(address?.postalCode || '', [Validators.required])
        });
    }

    get formControls() {
        return this.addressFormGroup.controls;
    }

    hideDialog() {
        this.addressDialog = false;
    }

    showAddressDialog(address?: Address) {
        this.initAddressFormGroup(address);
        this.addressDialog = true;
    }

    saveAddress() {
        if (this.addressFormGroup.invalid) {
            this.addressFormGroup.markAllAsTouched();
            return;
        }
        const address = this.addressFormGroup.value;
        this.loading.set(true);

        if (address.id) {
            this.addressService.updateAddress(address.id, address).subscribe({
                next: (address) => {
                    this.addresses.update((addressess) => addressess.map((add) => (add.id === address.id ? address : add)));
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Création',
                        detail: 'Adresse créée avec succès'
                    });
                    this.selectedAddress.set(address);
                    this.loading.set(false);
                    this.hideDialog();
                },
                error: (error) => {
                    this.loading.set(false);
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
                    this.selectedAddress.set(address);
                    this.loading.set(false);
                    this.hideDialog();
                },
                error: (error) => {
                    this.loading.set(false);
                    console.log(error);
                }
            });
        }
    }

    updateItemQuantity(id: number, quantity: number): void {
        if (this.timeoutMap[id]) {
            clearTimeout(this.timeoutMap[id]);
        }

        if (quantity == 0) {
            this.removeItem(id);
        } else {
            this.timeoutMap[id] = setTimeout(() => {
                this.shoppingCartService.updateItemQuantity(id, quantity).subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Succès',
                            detail: 'La quantité du produit a bien été modifiée.'
                        });
                        this.calculateTotalPrice();
                    },
                    error: (error) => console.error(error)
                });
            }, 1000);
        }
    }

    selectItem(id: number, selected: boolean) {
        this.shoppingCartService.selectItem(id, selected).subscribe({
            next: () => {
                this.calculateTotalPrice();
            },
            error: (error) => console.error(error)
        });
    }

    removeItem(id: number) {
        this.confirmationService.confirm({
            message: 'Êtes-vous sûr de vouloir supprimer ce produit du panier ?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.shoppingCartService.deleteItemFromShoppingCart(id).subscribe({
                    next: () => {
                        this.getShoppingCart();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Suppression',
                            detail: 'Le produit sélectionné a été supprimé du panier.',
                            life: 3000
                        });
                    },
                    error: (error) => {
                        console.error("Une erreur s'est produite lors de la suppression du produit:", error);
                    }
                });
            }
        });
    }

    calculateTotalPrice() {
        if (!this.shoppingCartService.shoppingCart()) return;
        if (this.shoppingCartService.shoppingCart()?.orderItems.length == 0) {
            this.totalPrice.set(0);
        } else {
            this.totalPrice.set(
                this.shoppingCartService
                    .shoppingCart()!
                    .orderItems.filter((item) => item.selected)
                    .reduce((acc, item) => acc + item.price * item.quantity, 0)
            );
        }
    }

    toggleCheckout() {
        this.checkout.update((checkout) => !checkout);
    }

    placeOrder() {
        const orderItems = this.shoppingCartService.shoppingCart()?.orderItems.filter((item) => item.selected) || [];
        if (orderItems.length === 0) {
            this.messageService.add({
                severity: 'error',
                summary: 'Erreur',
                detail: 'Veuillez choisir au moins un produit'
            });
            return;
        }
        if (!this.selectedAddress()) {
            this.messageService.add({
                severity: 'error',
                summary: 'Erreur',
                detail: 'Veuillez choisir une adresse de livraison'
            });
            return;
        }
        if (!this.selectedPaymentMethod) {
            this.messageService.add({
                severity: 'error',
                summary: 'Erreur',
                detail: 'Veuillez choisir un mode de payement'
            });
            return;
        }

        if (this.selectedPaymentMethod.toString() === 'CHEQUE' && !this.chequeNumber) {
            this.messageService.add({
                severity: 'error',
                summary: 'Erreur',
                detail: 'Veuillez choisir un numero de cheque'
            });
            return;
        }

        if (this.selectedPaymentMethod.toString() === 'CHEQUE' && !this.bankName) {
            this.messageService.add({
                severity: 'error',
                summary: 'Erreur',
                detail: 'Veuillez choisir un nom de banque'
            });
            return;
        }

        this.loading.set(true);

        const orderRequest = {
            userId: this.authService.currentUser()!.id!,
            orderItemsIds: orderItems.map((item) => item.id),
            paymentMethod: this.selectedPaymentMethod,
            deliveryAddressId: this.selectedAddress()!.id,
            chequeNumber: this.chequeNumber,
            bankName: this.bankName
        };

        this.orderService.placeOrder(orderRequest).subscribe({
            next: (order) => {
                this.getShoppingCart();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Création de commande',
                    detail: 'Votre commande a bien été créée, vous pouvez consulter votre historique de commandes',
                    life: 5000
                });
            },
            error: (error) => {
                console.log(error);
                this.loading.set(false);
            },
            complete: () => {
                this.loading.set(false);
                this.getShoppingCart();
            }
        });
    }

    disabled() {
        if (this.loading()) return true;
        if (!this.shoppingCartService.shoppingCart()) return true;
        if (this.shoppingCartService.shoppingCart()?.orderItems?.length == 0) return true;
        if (this.shoppingCartService.shoppingCart()?.orderItems?.filter((item) => item.selected).length == 0) return true;
        return false;
    }
}
