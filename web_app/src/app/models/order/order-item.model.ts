import { ProductColor } from '../product/product-color';

export interface OrderItem {
    id?: number;
    productId: number;
    productName: string;
    productImage: string;
    size: string;
    color: ProductColor;
    quantity: number;
    price: number;
    selected?: boolean;
}
