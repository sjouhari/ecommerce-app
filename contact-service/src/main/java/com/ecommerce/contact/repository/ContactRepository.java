package com.ecommerce.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.contact.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByUserId(Long userId);

}
