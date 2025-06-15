import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Category } from '../../../../models/category/category.model';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-list-categories',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './list-categories.component.html'
})
export class ListCategoriesComponent {
    categories = input.required<Category[]>();
    getCategorySubCategories(category: Category) {
        return category.subCategories.map((subCategory) => subCategory.name).join(', ');
    }
}
