import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Category } from '../../../models/category/category.model';
import { CategoryService } from '../../../services/category.service';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'categories-widget',
    standalone: true,
    imports: [CommonModule],
    template: ` <div id="categories" class="p-6" style="background: linear-gradient(0deg, rgba(255, 250, 255, 0.6), rgba(255, 250, 255, 0.6)), radial-gradient(77.36% 256.97% at 77.36% 57.52%, #efe1af 0%, #c3dcfa 100%)">
        <div class="grid grid-cols-12 gap-4 justify-center">
            <div class="col-span-12 text-center mb-3">
                <div class="text-surface-900 dark:text-surface-0 font-normal mb-2 text-4xl">Nos Catégories</div>
                <span class="text-muted-color text-2xl">Notre catalogue de produits</span>
            </div>

            @for (category of categories(); track category.id) {
                <div class="col-span-12 md:col-span-6 lg:col-span-3 p-0 mt-6 lg:mt-0">
                    <div style="height: 160px; padding: 2px; border-radius: 10px; background: linear-gradient(90deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2)), linear-gradient(180deg, rgba(253, 228, 165, 0.2), rgba(187, 199, 205, 0.2))">
                        <div class="p-4 bg-surface-0 dark:bg-surface-900 h-full" style="border-radius: 8px">
                            <div class="flex items-center justify-center bg-yellow-200 mb-4" style="width: 3.5rem; height: 3.5rem; border-radius: 10px">
                                <i class="pi pi-fw pi-users !text-2xl text-yellow-700"></i>
                            </div>
                            <h5 class="mb-2 text-surface-900 dark:text-surface-0">{{ category.name }}</h5>
                            <span class="text-surface-600 dark:text-surface-200">{{ category.description }}</span>
                        </div>
                    </div>
                </div>
            } @empty {
                <p class="col-span-12 text-center text-2xl">Aucune catégorie disponible, nous allons bientot ajouter des produits</p>
            }
        </div>
    </div>`
})
export class CategoriesWidget {
    categoryService = inject(CategoryService);

    categories = signal<Category[]>([]);

    ngOnInit() {
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories.set(categories);
            },
            error: (error) => {
                console.log(error); //TODO: handle error
            }
        });
    }
}
