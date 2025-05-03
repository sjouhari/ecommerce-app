import { Profil } from './role.model';

export interface User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    enabled: boolean;
    profils: Profil[];
}
