import { ProductColor } from './product-color';

export interface Stock {
    id: number;
    productId: number;
    size: string;
    color: ProductColor;
    price: number;
    quantity: number;
}
