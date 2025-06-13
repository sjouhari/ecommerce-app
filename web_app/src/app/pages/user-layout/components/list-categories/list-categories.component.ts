import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../../../services/category.service';
import { Category } from '../../../../models/category/category.model';

@Component({
    selector: 'app-list-categories',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './list-categories.component.html'
})
export class ListCategoriesComponent {
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
