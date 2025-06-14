import { Store } from '../user/store.model';
import { CommentModel } from './comment.model';
import { Media } from './media.model';
import { Stock } from './stock.model';
import { TVA } from './tva.model';

export interface Product {
    id: number;
    name: string;
    description: string;
    status: string;
    subCategoryId: number;
    subCategoryName: string;
    categoryId: number;
    categoryName: string;
    tva: TVA;
    medias: Media[];
    rating: number;
    stock: Stock[];
    comments?: CommentModel[];
    store?: Store;
}
