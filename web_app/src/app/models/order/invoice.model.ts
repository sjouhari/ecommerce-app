import { PaymentMethod } from './payment-method.model';

export interface Invoice {
    id: number;
    paymentMethod: PaymentMethod;
    totalPrice: number;
    createdAt: Date;
}
