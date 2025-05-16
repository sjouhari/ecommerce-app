import { Media } from './media.model';
import { Stock } from './stock.model';

export interface Product {
    id: number;
    name: string;
    description: string;
    status: string;
    subCategoryId: number;
    subCategoryName: string;
    categoryName: string;
    sellerId: number;
    medias: Media[];
    rating: number;
    stock: Stock[];
}
