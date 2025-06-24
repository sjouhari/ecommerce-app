import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contact } from '../models/contact/contact.model';

@Injectable({
    providedIn: 'root'
})
export class ContactService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/contacts';

    getContacts(): Observable<Contact[]> {
        return this.http.get<Contact[]>(this.baseUrl);
    }

    getContactById(id: number): Observable<Contact> {
        return this.http.get<Contact>(`${this.baseUrl}/${id}`);
    }

    sendMessage(contact: Contact): Observable<Contact> {
        return this.http.post<Contact>(this.baseUrl, contact);
    }

    responseContact(id: number, response: string): Observable<Contact> {
        return this.http.put<Contact>(`${this.baseUrl}/${id}`, { response });
    }

    deleteContact(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
