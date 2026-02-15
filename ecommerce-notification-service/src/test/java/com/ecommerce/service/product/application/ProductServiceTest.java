package com.ecommerce.service.product.application;

import application.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import application.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import application.ecommerce.service.product.adapter.exception.ProductNotFoundException;
import application.ecommerce.service.product.adapter.mapper.ProductMapper;
import application.ecommerce.service.product.application.ProductService;
import application.ecommerce.service.product.domain.model.Product;
import application.ecommerce.service.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductResponseById_shouldReturnProductResponseDTO_whenProductExists() throws ProductNotFoundException {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO("Test", java.math.BigDecimal.TEN, 10, "Desc", java.math.BigDecimal.valueOf(4.5));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.getProductResponseById(productId);

        assertNotNull(result);
        assertEquals(productResponseDTO, result);

        verify(productRepository).findById(productId);
        verify(productMapper).toResponse(product);
    }


    @Test
    void saveProduct_shouldReturnId_whenProductSaved() {
        ProductKafkaCreateDTO.ProductData productData = new ProductKafkaCreateDTO.ProductData("Test", 10.0, "Desc", 10, 4.5);
        Product productEntity = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(10L);

        when(productMapper.toEntity(productData)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedProduct);

        Long savedId = productService.saveProduct(productData);

        assertEquals(10L, savedId);

        verify(productMapper).toEntity(productData);
        verify(productRepository).save(productEntity);
    }
}
