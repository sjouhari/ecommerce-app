package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.TvaRepository;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.shared.dto.InventoryDto;
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

    @Override
    public List<ProductResponseDto> getAllProducts(String token) {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = ProductMapper.INSTANCE.productsToProductResponseDtos(products);
        return productResponseDtos.stream().map(
                productResponseDto -> {
                    List<InventoryDto> stock = inventoryApiClient.getInventoriesByProductId(productResponseDto.getId(), token);
                    productResponseDto.setStock(stock);
                    SubCategoryDto subCategoryDto = categoryApiClient.getSubCategoryById(productResponseDto.getSubCategoryId(), token);
                    CategoryDto categoryDto = categoryApiClient.getCategoryById(subCategoryDto.getCategoryId(), token);
                    productResponseDto.setCategoryName(categoryDto.getName());
                    productResponseDto.setSubCategoryName(subCategoryDto.getName());
                    return productResponseDto;
                }
        ).toList();
    }

    @Override
    public List<ProductResponseDto> getNewProducts(String token) {
        List<ProductResponseDto> productResponseDtos = ProductMapper.INSTANCE.productsToProductResponseDtos(productRepository.findAllNewProducts());
        return productResponseDtos.stream().map(
                productResponseDto -> {
                    List<InventoryDto> stock = inventoryApiClient.getInventoriesByProductId(productResponseDto.getId(), token);
                    productResponseDto.setStock(stock);
                    SubCategoryDto subCategoryDto = categoryApiClient.getSubCategoryById(productResponseDto.getSubCategoryId(), token);
                    CategoryDto categoryDto = categoryApiClient.getCategoryById(subCategoryDto.getCategoryId(), token);
                    productResponseDto.setCategoryName(categoryDto.getName());
                    productResponseDto.setSubCategoryName(subCategoryDto.getName());
                    return productResponseDto;
                }
        ).toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id, String token) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        List<InventoryDto> stock = inventoryApiClient.getInventoriesByProductId(id, token);
        SubCategoryDto subCategoryDto = categoryApiClient.getSubCategoryById(product.getSubCategoryId(), token);
        CategoryDto categoryDto = categoryApiClient.getCategoryById(subCategoryDto.getCategoryId(), token);
        ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);
        productResponseDto.setStock(stock);
        productResponseDto.setSubCategoryName(subCategoryDto.getName());
        productResponseDto.setCategoryName(categoryDto.getName());
        return productResponseDto;
    }

    @Override
    public ProductResponseDto createProduct(String productJson, List<MultipartFile> images, String token) {
        try {
            // Convert json to ProductRequestDto
            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDto productDto = mapper.readValue(productJson, ProductRequestDto.class);

            // Check if the product already exists by name
            if(productRepository.existsByName(productDto.getName())) {
                throw new RuntimeException("Product already exists with name: " + productDto.getName());
            }

            //TODO: Check if the category exists

            // Check if subcategory exists
            if(!categoryApiClient.subCategoryExistsById(productDto.getSubCategoryId(), token)) {
                throw new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId().toString());
            }

            // Check if sizes exist and belongs to the product category
            productDto.getStock()
                    .forEach(stock -> {
                        if(!categoryApiClient.sizeExistsByLibelleAndCategoryId(stock.getSize(), productDto.getCategoryId(), token)) {
                            throw new ResourceNotFoundException("Size", "name", stock.getSize());
                        }
                    });

            Product product = ProductMapper.INSTANCE.productRequestDtoToProduct(productDto);
            Tva tva = tvaRepository.findById(productDto.getTva().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Tva", "id", productDto.getTva().getId().toString())
            );
            product.setTva(tva);

            List<Media> medias = new ArrayList<>();
            for (MultipartFile image : images) {
                String fileName = fileStorageService.saveImage(image);
                Media media = new Media();
                media.setUrl(fileName);
                media.setAltText(product.getName());
                media.setProduct(product);
                medias.add(media);
            }
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
            // Convert json to ProductRequestDto
            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDto productDto = mapper.readValue(productJson, ProductRequestDto.class);

            Product productToUpdate = productRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Product", "id", id.toString())
            );

            // Check if the product already exists by name
            if(!productToUpdate.getName().equals(productDto.getName()) && productRepository.existsByName(productDto.getName())) {
                throw new RuntimeException("Product already exists with name: " + productDto.getName());
            }

            // Check if subcategory exists
            if(!categoryApiClient.subCategoryExistsById(productDto.getSubCategoryId(), token)) {
                throw new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId().toString());
            }

            // Check if sizes exist and belongs to the product category
            productDto.getStock()
                    .forEach(stock -> {
                        if(!categoryApiClient.sizeExistsByLibelleAndCategoryId(stock.getSize(), productDto.getCategoryId(), token)) {
                            throw new ResourceNotFoundException("Size", "name", stock.getSize());
                        }
                    });

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
                for (MultipartFile image : newImages) {
                    String fileName = fileStorageService.saveImage(image);
                    Media media = new Media();
                    media.setUrl(fileName);
                    media.setAltText(product.getName());
                    media.setProduct(product);
                    product.getMedias().add(media);
                }
            }

            // Delete images
            if(deletedImages != null) {
                List<String> deletedImageList = List.of(deletedImages.split(","));
                for (String fileName : deletedImageList) {
                    fileStorageService.deleteImage(fileName);
                    product.getMedias().removeIf(media -> media.getUrl().equals(fileName));
                }
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
