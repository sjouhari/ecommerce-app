package com.ecommerce.email.controller;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.dto.NewsletterDto;
import com.ecommerce.email.dto.ProductItem;
import com.ecommerce.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
        emailService.sendEmail(emailDto);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/newsletter")
    public ResponseEntity<String> sendNewsletterEmail(@RequestBody NewsletterDto newsletterDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(newsletterDto.getEmail());
        emailDto.setSubject("Newsletter - Our new products");
        String products = newsletterDto.getProducts().stream().map(ProductItem::toString).reduce("", String::concat);
        emailDto.setBody("Hello, \n\nWe have some new products for you :\n " + products);
        emailService.sendEmail(emailDto);
        return ResponseEntity.ok("Email sent successfully");
    }

}
