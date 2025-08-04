package com.ecommerce.service.product.application;

import com.ecommerce.service.product.domain.repository.ProductRepository;
import com.ecommerce.service.product.adapter.mapper.ProductMapper;
import com.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import com.ecommerce.service.product.domain.model.Product;
import com.ecommerce.service.product.adapter.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper; // Mapper inyectado (MapStruct o manual)

    private Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductResponseDTO getProductResponseById(Long id) throws ProductNotFoundException {
        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        return productMapper.toResponse(product);
    }

}
