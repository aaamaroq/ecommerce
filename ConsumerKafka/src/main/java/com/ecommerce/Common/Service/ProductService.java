package com.ecommerce.Common.Service;


import com.ecommerce.Common.Model.Product;
import com.ecommerce.Common.Repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired(required=true)
    private ProductRepository productRepository;

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}