import { PaymentMethods } from './payment-methods';

export interface OrderRequest {
    userId: number;
    storeId?: number;
    storeName?: string;
    userName?: string;
    deliveryAddressId: number;
    orderItemsIds: (number | undefined)[];
    paymentMethod: PaymentMethods;
    chequeNumber?: string;
    bankName?: string;
}
