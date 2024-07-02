package com.ecommerce.ApiECommerce.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ApiECommerce.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
