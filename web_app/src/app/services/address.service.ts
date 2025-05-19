import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Address } from '../models/order/address.model';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AddressService {
    httpClient = inject(HttpClient);

    private baseUrl = 'http://localhost:8080/api/addresses';

    getUserAddresses(userId: number): Observable<Address[]> {
        return this.httpClient.get<Address[]>(`${this.baseUrl}/user/${userId}`);
    }

    getAddress(addressId: number): Observable<Address> {
        return this.httpClient.get<Address>(`${this.baseUrl}/${addressId}`);
    }

    createAddress(address: Address): Observable<Address> {
        return this.httpClient.post<Address>(`${this.baseUrl}`, address);
    }

    updateAddress(addressId: number, address: Address): Observable<Address> {
        return this.httpClient.put<Address>(`${this.baseUrl}/${addressId}`, address);
    }

    deleteAddress(addressId: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.baseUrl}/${addressId}`);
    }
}
