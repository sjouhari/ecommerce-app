package com.ecommerce.contact.mapper;

import com.ecommerce.shared.dto.ContactDto;
import com.ecommerce.contact.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDto contactToContactDto(Contact contact);

    Contact contactDtoToContact(ContactDto contactDto);

    List<ContactDto> contactListToContactDtoList(List<Contact> contactList);

}
