package com.ecommerce.contact.service;

import com.ecommerce.shared.dto.ContactDto;

import java.util.List;

public interface ContactService {

    List<ContactDto> getAllContacts();

    ContactDto getContactById(Long id);

    ContactDto createContact(ContactDto contactDto, String token);

    ContactDto updateContact(Long id, ContactDto contactDto);

    String deleteContact(Long id);

    List<ContactDto> getContactsByUserId(Long userId);

}
