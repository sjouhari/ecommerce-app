import { MessageService } from 'primeng/api';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../models/product/product.model';
import { ProductService } from '../../services/product.service';
import { GalleriaModule } from 'primeng/galleria';
import { ButtonModule } from 'primeng/button';
import { Media } from '../../models/product/media.model';
import { FormsModule } from '@angular/forms';
import { ProductColor } from '../../models/product/product-color';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumber } from 'primeng/inputnumber';
import { Location } from '@angular/common';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { AuthService } from '../../services/auth.service';
import { ToastModule } from 'primeng/toast';

@Component({
    selector: 'app-product-details',
    imports: [GalleriaModule, ButtonModule, RadioButtonModule, InputNumber, FormsModule, ToastModule],
    templateUrl: './product-details.component.html',
    providers: [MessageService]
})
export class ProductDetailsComponent implements OnInit {
    route = inject(ActivatedRoute);
    productService = inject(ProductService);
    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    location = inject(Location);

    images: Media[] = [];
    colors: ProductColor[] = [];
    sizes: string[] | undefined = [];

    selectedColor: ProductColor | null = null;
    selectedSize: string | null = null;
    selectedQuantity: number = 1;
    totalPrice = signal<number>(0);
    quantityInStock = signal<number>(0);

    product = signal<Product | null>(null);
    _activeIndex: number = 0;

    get activeIndex(): number {
        return this._activeIndex;
    }

    set activeIndex(newValue) {
        if (this.product()!.medias && 0 <= newValue && newValue <= this.product()!.medias.length - 1) {
            this._activeIndex = newValue;
        }
    }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.productService.getProduct(id).subscribe({
                next: (product) => {
                    this.product.set(product);
                    this.images = product.medias;
                    this.colors = product.stock?.map((stock) => stock.color) || [];
                    this.sizes = [...new Set(product.stock?.map((stock) => stock.size))];
                    this.totalPrice.set(product.stock[0].price);
                    this.selectedColor = product.stock[0].color;
                    this.selectedSize = product.stock[0].size;
                    this.quantityInStock.set(product.stock[0].quantity);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }

    goBack() {
        this.location.back();
    }

    responsiveOptions: any[] = [
        {
            breakpoint: '1300px',
            numVisible: 4
        },
        {
            breakpoint: '575px',
            numVisible: 1
        }
    ];

    next() {
        this.activeIndex++;
    }

    prev() {
        this.activeIndex--;
    }

    addToCart() {
        if (this.product() && this.selectedSize && this.selectedColor) {
            this.shoppingCartService
                .addItemToShoppingCart(this.authService.getCurrentUser()?.id!, {
                    productId: this.product()!.id,
                    productName: this.product()!.name,
                    productImage: this.product()!.medias[0].url,
                    size: this.selectedSize,
                    color: this.selectedColor,
                    quantity: this.selectedQuantity,
                    price: this.product()!.stock!.find((stock) => stock.color === this.selectedColor && stock.size === this.selectedSize)!.price
                })
                .subscribe({
                    next: (cart) => {
                        this.shoppingCartService.setShoppingCart(cart);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Ajout au panier',
                            detail: 'Produit ajouté au panier'
                        });
                    },
                    error: (error) => {
                        console.log(error); //TODO: handle error
                    }
                });
        }
    }

    calculateTotalePrice() {
        let stock = this.product()?.stock;
        stock = stock!.filter((stock) => stock.color === this.selectedColor && stock.size === this.selectedSize);
        this.quantityInStock.set(stock![0].quantity);
        this.totalPrice.set(stock![0].price * this.selectedQuantity);
    }

    disableColor(color: string) {
        return !this.selectedSize || !this.product()!.stock?.some((stock) => stock.size === this.selectedSize && stock.color === color);
    }
}
