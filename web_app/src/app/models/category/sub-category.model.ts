import { Category } from './category.model';

export interface SubCategory {
    id: number;
    name: string;
    description: string;
    category: Category;
}
