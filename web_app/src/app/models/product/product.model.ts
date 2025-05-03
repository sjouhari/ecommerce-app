import { Stock } from './stock.model';

export interface Product {
    id?: string;
    name?: string;
    description?: string;
    price?: number;
    status?: string;
    subCategoryId?: number;
    medias?: string[];
    rating?: number;
    stock?: Stock[];
}
