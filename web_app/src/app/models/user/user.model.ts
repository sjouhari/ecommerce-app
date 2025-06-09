import { Profil } from './profil.model';
import { Store } from './store.model';

export interface User {
    id?: number;
    firstName: string;
    lastName: string;
    email: string;
    enabled?: boolean;
    profils?: Profil[];
    store?: Store;
    createdAt?: Date;
    updatedAt?: Date;
}
