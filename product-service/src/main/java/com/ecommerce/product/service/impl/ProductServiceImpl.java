package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.InventoryDto;
import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.dto.StockDto;
import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.TvaRepository;
import com.ecommerce.product.service.ProductService;
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
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.INSTANCE.productsToProductResponseDtos(products);
    }

    @Override
    public ProductResponseDto getProductById(Long id, String token) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        List<InventoryDto> inventories = inventoryApiClient.getInventoriesByProductId(id, token);
        List<StockDto> stocks = ProductMapper.INSTANCE.inventoryDtosToStockDtos(inventories);
        ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);
        productResponseDto.setStock(stocks);
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

            // Check if subcategory exists
            if(!categoryApiClient.categoryExistsById(productDto.getSubCategoryId(), token)) {
                throw new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId().toString());
            }

            // Check if sizes exist and belongs to subCategory
            productDto.getStock()
                    .forEach(stock -> {
                        if(!categoryApiClient.sizeExistsByLibelleAndSubCategoryId(stock.getSize(), productDto.getSubCategoryId(), token)) {
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
                String fileName = fileStorageService.saveProductImages(image);
                Media media = new Media();
                media.setUrl(fileName);
                media.setAltText(product.getName());
                media.setProduct(product);
                medias.add(media);
            }
            product.setMedias(medias);

            Product savedProduct = productRepository.save(product);
            // Create product in inventory service
            List<InventoryDto> inventoryDtos = new ArrayList<>();
            productDto.getStock().forEach(
                    stock -> {
                        InventoryDto inventoryDto = new InventoryDto();
                        inventoryDto.setProductId(savedProduct.getId());
                        inventoryDto.setSize(stock.getSize());
                        inventoryDto.setColor(stock.getColor());
                        inventoryDto.setQuantity(stock.getQuantity());
                        inventoryDto.setPrice(stock.getPrice());
                        InventoryDto createdInventory = inventoryApiClient.createInventory(inventoryDto, token);
                        inventoryDtos.add(createdInventory);
                    }
            );
            ProductResponseDto productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(savedProduct);
            productResponseDto.setStock(ProductMapper.INSTANCE.inventoryDtosToStockDtos(inventoryDtos));
            return productResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productDto) {
        productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        Product product = ProductMapper.INSTANCE.productRequestDtoToProduct(productDto);
        Tva tva = tvaRepository.findById(productDto.getTva().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Tva", "id", productDto.getTva().getId().toString())
        );
        product.setTva(tva);
        product.getMedias().forEach(media -> media.setProduct(product));
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductResponseDto(updatedProduct);
    }

    @Override
    public String deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }

    @Override
    public boolean productExistsById(Long id) {
        return productRepository.existsById(id);
    }
}
