import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product/product.model';
import { Store } from '../../models/user/store.model';
import { StoreService } from '../../services/store.service';
import { ProductListComponent } from '../../user-layout/components/product-list/product-list.component';

@Component({
    selector: 'app-stores',
    imports: [ProductListComponent],
    templateUrl: './store-products.component.html'
})
export class StoreProductsComponent implements OnInit {
    route = inject(ActivatedRoute);
    storeService = inject(StoreService);
    productService = inject(ProductService);

    store: Store | null = null;
    products: Product[] = [];

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');

        if (id) {
            this.storeService.getStore(+id).subscribe({
                next: (store) => {
                    this.store = store;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
            this.productService.getProductsByStoreId(+id).subscribe({
                next: (products) => {
                    this.products = products;
                },
                error: (error) => {
                    console.log(error); //TODO: handle error
                }
            });
        }
    }
}
