package com.ecommerce.email.controller;

import com.ecommerce.email.dto.EmailDto;
import com.ecommerce.email.dto.NewsletterDto;
import com.ecommerce.email.dto.ProductItem;
import com.ecommerce.email.service.EmailService;
import com.ecommerce.email.service.NewsletterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
        emailService.sendEmail(emailDto, Map.of("message", "Hello World"));
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/newsletter")
    public ResponseEntity<String> sendNewsletterEmail(@RequestBody NewsletterDto newsletterDto) {
        newsletterService.sendEmail(newsletterDto);
        return ResponseEntity.ok("Email sent successfully");
    }

}
