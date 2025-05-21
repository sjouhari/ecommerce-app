import { PaymentMethod } from './payment-methods';

export interface OrderRequest {
    userId: number;
    deliveryAddressId: number;
    orderItemsIds: (number | undefined)[];
    paymentMethod: PaymentMethod;
    chequeNumber?: string;
    bankName?: string;
}
