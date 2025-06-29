import { MessageService } from 'primeng/api';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { GalleriaModule } from 'primeng/galleria';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumber } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { FloatLabelModule } from 'primeng/floatlabel';
import { TextareaModule } from 'primeng/textarea';
import { ProductService } from '../../services/product.service';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { AuthService } from '../../services/auth.service';
import { CommentService } from '../../services/comment.service';
import { Media } from '../../models/product/media.model';
import { ProductColor } from '../../models/product/product-color';
import { CommentModel } from '../../models/product/comment.model';
import { Product } from '../../models/product/product.model';
import { ProductListComponent } from '../../user-layout/components/product-list/product-list.component';

@Component({
    selector: 'app-product-details',
    imports: [GalleriaModule, ButtonModule, RadioButtonModule, InputNumber, FormsModule, ToastModule, FloatLabelModule, TextareaModule, RouterLink, ProductListComponent],
    templateUrl: './product-details.component.html',
    providers: [MessageService]
})
export class ProductDetailsComponent implements OnInit {
    route = inject(ActivatedRoute);
    productService = inject(ProductService);
    shoppingCartService = inject(ShoppingCartService);
    authService = inject(AuthService);
    messageService = inject(MessageService);
    commentService = inject(CommentService);

    images: Media[] = [];
    allColors = Object.entries(ProductColor).map(([key, value]) => ({ key, value }));
    colors: { key: string; value: string }[] = [];
    sizes: string[] | undefined = [];
    comment: string = '';
    comments = signal<CommentModel[]>([]);
    relatedProducts = signal<Product[]>([]);
    loading = signal<boolean>(false);

    selectedColor: string | null = null;
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
        this.route.paramMap.subscribe((params) => {
            const id = params.get('id')!;
            this.productService.getProduct(id).subscribe({
                next: (product) => {
                    this.product.set(product);
                    this.images = product.medias;
                    this.sizes = [...new Set(product.stock?.map((stock) => stock.size))];
                    this.totalPrice.set(product.stock[0].price);
                    this.selectedSize = product.stock[0].size;
                    this.getSizeColors();
                    this.getColorQuantity();
                    this.getProductComments(+id);
                    this.getRelatedProducts(product.categoryId);
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        });
    }

    getProductComments(productId: number) {
        this.commentService.getProductComments(productId).subscribe({
            next: (comments) => {
                this.comments.set(comments);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getRelatedProducts(categoryId: number) {
        this.productService.getProductsByCategory(categoryId).subscribe({
            next: (products) => {
                this.relatedProducts.set(products.filter((product) => product.id !== this.product()?.id));
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }

    getSizeColors() {
        const productColors: string[] = [];
        this.product()?.stock?.forEach((stock) => {
            if (stock.size === this.selectedSize) {
                productColors.push(stock.color);
            }
        });
        this.colors = this.allColors.filter((color) => productColors.includes(color.key));
        this.selectedColor = this.colors[0].key;
    }

    getColorQuantity() {
        this.product()?.stock?.forEach((stock) => {
            if (stock.size === this.selectedSize && stock.color === this.selectedColor) {
                this.quantityInStock.set(stock.quantity);
                if (this.selectedQuantity > this.quantityInStock()) {
                    this.selectedQuantity = this.quantityInStock();
                }
            }
        });
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
            this.loading.set(true);
            this.shoppingCartService
                .addItemToShoppingCart(this.authService.currentUser()?.id!, {
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
                    },
                    complete: () => {
                        this.shoppingCartService.getCurrentUserShoppingCart();
                        this.loading.set(false);
                    }
                });
        }
    }

    onSizeChange() {
        this.getSizeColors();
        this.getColorQuantity();
        this.onQuantityChange();
    }

    onColorChange(color: string) {
        this.selectedColor = color;
        this.getColorQuantity();
        this.onQuantityChange();
    }

    onQuantityChange() {
        if (this.selectedQuantity > this.quantityInStock()) {
            this.selectedQuantity = this.quantityInStock();
        }
        this.totalPrice.set(this.selectedQuantity * this.product()!.stock!.find((stock) => stock.color === this.selectedColor && stock.size === this.selectedSize)!.price);
    }

    addComment() {
        if (!this.comment) {
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Veuillez saisir un commentaire' });
            return;
        }

        const commentModel: CommentModel = {
            productId: this.product()!.id,
            userId: this.authService.currentUser()!.id!,
            content: this.comment,
            rating: 5
        };

        this.commentService.createComment(commentModel).subscribe({
            next: (comment) => {
                this.getProductComments(this.product()!.id);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Commentaire ajouté',
                    detail: 'Votre commentaire a été ajouté avec succès'
                });
                this.comment = '';
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
