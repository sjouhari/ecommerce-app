import { PaymentMethod } from './payment-methods';

export interface OrderRequest {
    userId: number;
    userName: string;
    deliveryAddressId: number;
    orderItemsIds: (number | undefined)[];
    paymentMethod: PaymentMethod;
    chequeNumber?: string;
    bankName?: string;
}
