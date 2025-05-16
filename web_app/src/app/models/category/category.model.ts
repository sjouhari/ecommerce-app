import { Size } from './size.model';
import { SubCategory } from './sub-category.model';

export interface Category {
    id: number | null;
    name: string;
    description: string;
    subCategories: SubCategory[];
    sizes: Size[];
}
