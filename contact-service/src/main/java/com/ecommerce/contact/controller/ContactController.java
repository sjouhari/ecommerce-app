package com.ecommerce.contact.controller;

import com.ecommerce.contact.dto.ContactDto;
import com.ecommerce.contact.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Validated
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactDto>> getContactsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(contactService.getContactsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestBody @Valid ContactDto contactDto, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(contactService.createContact(contactDto, token), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable Long id, @RequestBody @Valid ContactDto contactDto) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

}
