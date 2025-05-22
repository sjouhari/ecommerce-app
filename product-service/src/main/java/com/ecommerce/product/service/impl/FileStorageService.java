package com.ecommerce.product.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.images.path}")
    private String imagesPath;

    public String saveImage(MultipartFile file) {
        try {
            String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension;
            Path path = Paths.get(imagesPath);
            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save product image", e);
        }
    }

    public void deleteImage(String fileName) {
        try {
            // Empêche les chemins malveillants
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file name: " + fileName);
            }

            Path filePath = Paths.get(imagesPath).resolve(fileName).normalize();

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                // Optionnel : logger que le fichier n'existe pas
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete product image: " + fileName, e);
        }
    }

}
