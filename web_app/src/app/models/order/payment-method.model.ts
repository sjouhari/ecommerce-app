import { PaymentStatus } from './payment-status';

export interface PaymentMethod {
    id: number;
    status: PaymentStatus;
    type: PaymentMethod;
    chequeNumber?: string;
    bankName?: string;
}
