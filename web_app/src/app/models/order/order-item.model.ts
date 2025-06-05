import { ProductColor } from '../product/product-color';

export interface OrderItem {
    id?: number;
    productId: number;
    productName: string;
    productImage: string;
    size: string;
    color: string;
    quantity: number;
    price: number;
    selected?: boolean;
}
