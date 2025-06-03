import { Profil } from './profil.model';

export interface User {
    id?: number;
    firstName: string;
    lastName: string;
    email: string;
    enabled?: boolean;
    profils?: Profil[];
}
