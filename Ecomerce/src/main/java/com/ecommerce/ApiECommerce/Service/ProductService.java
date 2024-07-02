package com.ecommerce.ApiECommerce.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ApiECommerce.Model.Product;
import com.ecommerce.ApiECommerce.Repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product findProductoById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}