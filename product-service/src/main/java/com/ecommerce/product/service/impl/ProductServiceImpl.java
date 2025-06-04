package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.exception.ProductNameAlreadyExistsException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.MediaRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.TvaRepository;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.shared.dto.InventoryDto;
import com.ecommerce.shared.dto.CategoryDto;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TvaRepository tvaRepository;

    @Autowired
    private CategoryApiClient categoryApiClient;

    @Autowired
    private InventoryApiClient inventoryApiClient;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MediaRepository mediaRepository;

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(
                this::getProductResponseDto
        ).toList();
    }

    @Override
    public List<ProductResponseDto> getNewProducts() {
        List<Product> products = productRepository.findAllNewProducts();
        return products.stream().map(
                this::getProductResponseDto
        ).toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        return getProductResponseDto(product);
    }

    private ProductResponseDto getProductResponseDto(Product product) {
        List<InventoryDto> stock = inventoryApiClient.getInventoriesByProductId(product.getId());
        SubCategoryDto subCategoryDto = categoryApiClient.getSubCategoryById(product.getSubCategoryId());
        CategoryDto categoryDto = categoryApiClient.getCategoryById(subCategoryDto.getCategory().getId());
        ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);

        productResponseDto.setStock(stock);
        productResponseDto.setSubCategoryName(subCategoryDto.getName());
        productResponseDto.setCategoryName(categoryDto.getName());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto createProduct(String productJson, List<MultipartFile> images, String token) {
        try {
            ProductRequestDto productDto = getProductRequestDto(productJson);

            // Check if the product already exists by name
            validateProductName(productDto.getName());
            validateProductCategoryDetails(productDto);

            Product product = ProductMapper.INSTANCE.productRequestDtoToProduct(productDto);
            Tva tva = tvaRepository.findById(productDto.getTva().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Tva", "id", productDto.getTva().getId().toString())
            );
            product.setTva(tva);

            List<Media> medias = saveProductImages(images, product);
            product.setMedias(medias);

            Product savedProduct = productRepository.save(product);
            // Create product in inventory service
            List<InventoryDto> stock = productDto.getStock().stream().map(
                    inventoryDto -> {
                        inventoryDto.setProductId(savedProduct.getId());
                        return inventoryApiClient.createInventory(inventoryDto, token);
                    }
            ).toList();
            ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(savedProduct);
            productResponseDto.setStock(stock);
            return productResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductResponseDto updateProduct(Long id, String productJson, List<MultipartFile> newImages, String deletedImages, String token) {
        try {
            ProductRequestDto productDto = getProductRequestDto(productJson);

            Product productToUpdate = productRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "id", id.toString())
            );

            // Check if the product already exists by name
            if(!productToUpdate.getName().equals(productDto.getName())) {
                validateProductName(productDto.getName());
            }
            validateProductCategoryDetails(productDto);

            Tva tva = tvaRepository.findById(productDto.getTva().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Tva", "id", productDto.getTva().getId().toString())
            );

            // Update product
            Product product = ProductMapper.INSTANCE.productRequestDtoToProduct(productDto);
            product.setId(id);
            product.setTva(tva);

            // Save new images
            product.setMedias(productToUpdate.getMedias());
            if(newImages != null) {
                List<Media> medias = saveProductImages(newImages, product);
                product.getMedias().addAll(medias);
            }

            // Delete images
            if (deletedImages != null) {
                List<String> deletedImageList = List.of(deletedImages.split(","));

                deletedImageList.forEach(fileStorageService::deleteImage);

                product.setMedias(
                        product.getMedias().stream()
                                .filter(media -> !deletedImageList.contains(media.getUrl()))
                                .toList()
                );
            }

            Product savedProduct = productRepository.save(product);
            // Update product stock
            List<InventoryDto> stock = productDto.getStock().stream().map(
                    inventoryDto -> inventoryApiClient.updateInventory(inventoryDto.getId(), inventoryDto, token)
            ).toList();
            ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(savedProduct);
            productResponseDto.setStock(stock);
            return productResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ProductRequestDto getProductRequestDto(String productJson) throws JsonProcessingException {
        // Convert json to ProductRequestDto
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(productJson, ProductRequestDto.class);
    }

    private void validateProductName(String productName) {
        if(productRepository.existsByName(productName)) {
            throw new ProductNameAlreadyExistsException("Le nom du produit existe déjà :  " + productName);
        }
    }

    private void validateProductCategoryDetails(ProductRequestDto productDto) {
        // Check if category exists
        if(!categoryApiClient.categoryExistsById(productDto.getCategoryId())) {
            throw new ResourceNotFoundException("Category", "id", productDto.getCategoryId().toString());
        }

        // Check if subcategory exists
        if(!categoryApiClient.subCategoryExistsById(productDto.getSubCategoryId())) {
            throw new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId().toString());
        }

        // Check if sizes exist and belongs to the product category
        productDto.getStock()
                .forEach(stock -> {
                    if(!categoryApiClient.sizeExistsByLibelleAndCategoryId(stock.getSize(), productDto.getCategoryId())) {
                        throw new ResourceNotFoundException("Size", "name", stock.getSize());
                    }
                });
    }

    private List<Media> saveProductImages(List<MultipartFile> images, Product product) {
        List<Media> medias = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = fileStorageService.saveImage(image);
            Media media = new Media();
            media.setUrl(fileName);
            media.setAltText(product.getName());
            media.setProduct(product);
            medias.add(media);
        }
        return medias;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        productRepository.deleteById(id);
    }

    @Override
    public boolean productExistsById(Long id) {
        return productRepository.existsById(id);
    }

}
