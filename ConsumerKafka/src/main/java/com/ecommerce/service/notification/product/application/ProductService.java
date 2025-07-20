package com.ecommerce.notification.product.application;

import com.ProductRepository;
import com.ecommerce.notification.product.adapter.mapper.ProductMapper;
import com.ecommerce.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.notification.product.domain.model.Product;
import com.ecommerce.notification.product.exception.ProductNotFoundException;
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

    public ProductResponse getProductResponseById(Long id) throws ProductNotFoundException {
        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        return productMapper.toResponse(product);
    }

}
