package com.ecommerce.contact.service.impl;

import com.ecommerce.contact.dto.ContactResponseDto;
import com.ecommerce.contact.kafka.ContactProducer;
import com.ecommerce.shared.dto.ContactDto;
import com.ecommerce.contact.entity.Contact;
import com.ecommerce.contact.mapper.ContactMapper;
import com.ecommerce.contact.repository.ContactRepository;
import com.ecommerce.contact.service.ContactService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final UserApiClient userApiClient;

    private final ContactProducer contactProducer;

    public ContactServiceImpl(ContactRepository contactRepository, UserApiClient userApiClient, ContactProducer contactProducer) {
        this.contactRepository = contactRepository;
        this.userApiClient = userApiClient;
        this.contactProducer = contactProducer;
    }

    @Override
    public List<ContactDto> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return ContactMapper.INSTANCE.contactListToContactDtoList(contacts);
    }

    @Override
    public ContactDto getContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contact", "id", id.toString())
        );
        return ContactMapper.INSTANCE.contactToContactDto(contact);
    }

    @Override
    public ContactDto createContact(ContactDto contactDto, String token) {
        if(!userApiClient.userExistsById(contactDto.getUserId(), token)) {
            throw new ResourceNotFoundException("User", "id", contactDto.getUserId().toString());
        }
        Contact contact = ContactMapper.INSTANCE.contactDtoToContact(contactDto);
        Contact savedContact = contactRepository.save(contact);
        contactProducer.sendMessage(contactDto, "contact_new");
        return ContactMapper.INSTANCE.contactToContactDto(savedContact);
    }

    @Override
    public ContactDto responseContact(Long id, ContactResponseDto contactResponseDto) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contact", "id", id.toString())
        );
        contact.setResponse(contactResponseDto.getResponse());
        contact.setId(id);
        Contact savedContact = contactRepository.save(contact);
        contactProducer.sendMessage(ContactMapper.INSTANCE.contactToContactDto(savedContact), "contact_response");
        return ContactMapper.INSTANCE.contactToContactDto(savedContact);
    }

    @Override
    public String deleteContact(Long id) {
        getContactById(id);
        contactRepository.deleteById(id);
        return "Contact deleted successfully";
    }

    @Override
    public List<ContactDto> getContactsByUserId(Long userId) {
        List<Contact> contacts = contactRepository.findAllByUserId(userId);
        return ContactMapper.INSTANCE.contactListToContactDtoList(contacts);
    }

}
