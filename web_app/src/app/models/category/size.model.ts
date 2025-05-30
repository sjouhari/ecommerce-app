import { Category } from './category.model';

export interface Size {
    id: number;
    libelle: string;
    category: Category;
}
