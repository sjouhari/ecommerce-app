import { Size } from './size.model';

export interface SubCategory {
    id: number;
    categoryId: number;
    name: string;
    description: string;
    sizes: Size[];
}
