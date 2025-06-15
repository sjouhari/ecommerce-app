import { Component, inject } from '@angular/core';
import { Product } from '../../models/product/product.model';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { ProductListComponent } from '../../user-layout/components/product-list/product-list.component';
import { Category } from '../../models/category/category.model';

@Component({
    selector: 'app-category-products',
    imports: [ProductListComponent],
    templateUrl: './category-products.component.html'
})
export class CategoryProductsComponent {
    products: Product[] = [];

    private readonly route = inject(ActivatedRoute);
    productService = inject(ProductService);

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        this.productService.getProductsByCategory(+id!).subscribe({
            next: (products) => {
                this.products = products;
            },
            error: (err) => console.error(err)
        });
    }
}
