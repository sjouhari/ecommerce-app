package com.ecommerce.contact.service;

import com.ecommerce.contact.dto.ContactResponseDto;
import com.ecommerce.shared.dto.ContactDto;

import java.util.List;

public interface ContactService {

    List<ContactDto> getAllContacts();

    ContactDto getContactById(Long id);

    ContactDto createContact(ContactDto contactDto, String token);

    ContactDto responseContact(Long id, ContactResponseDto contactResponseDto);

    String deleteContact(Long id);

    List<ContactDto> getContactsByUserId(Long userId);

}
