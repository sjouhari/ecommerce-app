package com.ecommerce.email.service;

import com.ecommerce.email.dto.NewsletterDto;
import com.ecommerce.email.dto.ProductItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NewsletterService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsletterService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public NewsletterService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(NewsletterDto newsletterDto) {
        Context context = new Context();
        context.setVariable("subject", "Newsletter - Nos nouveaux produits");
        context.setVariable("name", newsletterDto.getName());
        context.setVariable("message", "Voici la liste de nos nouveaux produits");
        context.setVariable("products", newsletterDto.getProducts());

        String htmlContent = templateEngine.process("email-template", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set basic email properties first
            helper.setTo(newsletterDto.getEmail());
            helper.setSubject("Newsletter - Nos nouveaux produits");
            helper.setText(htmlContent, true);

            // Get path to sibling images folder
            String projectRoot = System.getProperty("user.dir");
            Path imagesPath = Paths.get(projectRoot).resolve("images/products/");

            // Add inline images after setting the text content
            for (ProductItem product : newsletterDto.getProducts()) {
                try {
                    Path imagePath = imagesPath.resolve(product.getImageUrl());
                    FileSystemResource resource = new FileSystemResource(imagePath);

                    if (resource.exists()) {
                        helper.addInline(product.getImageUrl(), resource);
                    } else {
                        LOGGER.warn("Image not found: {}", imagePath);
                        // Optionally add a placeholder image
                    }
                } catch (Exception e) {
                    LOGGER.error("Error attaching image {}: {}", product.getImageUrl(), e.getMessage());
                }
            }

            mailSender.send(message);
            LOGGER.info("Email sent to {} successfully", newsletterDto.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email to {}", newsletterDto.getEmail(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}