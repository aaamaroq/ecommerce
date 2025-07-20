package com.ecommerce.service.notification.product.domain.repository;

import com.ecommerce.service.notification.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
