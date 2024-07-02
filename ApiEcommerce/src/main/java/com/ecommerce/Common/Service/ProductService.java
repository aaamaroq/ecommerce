package com.ecommerce.Common.Service;


import com.ecommerce.Common.Model.Product;
import com.ecommerce.Common.Repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product findProductoById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}