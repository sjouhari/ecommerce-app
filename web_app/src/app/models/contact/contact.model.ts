export interface Contact {
    id?: number;
    name: string;
    email: string;
    phone: string;
    subject: string;
    message: string;
    response?: string;
    userId: number;
    createdAt?: Date;
    updatedAt?: Date;
}
