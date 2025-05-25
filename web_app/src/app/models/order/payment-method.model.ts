import { PaymentMethods } from './payment-methods';
import { PaymentStatus } from './payment-status';

export interface PaymentMethod {
    id: number;
    status: PaymentStatus;
    type: PaymentMethods;
    chequeNumber?: string;
    bankName?: string;
}
