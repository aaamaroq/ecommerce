package com.ecommerce.product.application;


import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}