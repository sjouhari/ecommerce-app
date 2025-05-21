import { Address } from './address.model';
import { Invoice } from './invoice.model';
import { OrderItem } from './order-item.model';
import { OrderStatus } from './order-status';

export interface Order {
    id?: number;
    userId: number;
    status?: OrderStatus;
    invoice: Invoice;
    deliveryAddress: Address;
    orderItems: OrderItem[];
}
