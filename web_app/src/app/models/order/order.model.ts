import { Address } from './address.model';
import { OrderItem } from './order-item.model';
import { OrderStatus } from './order-status';

export interface Order {
    id?: number;
    userId: number;
    status?: OrderStatus;
    modePayment: string;
    deliveryAddress: Address;
    orderItems: OrderItem[];
}
