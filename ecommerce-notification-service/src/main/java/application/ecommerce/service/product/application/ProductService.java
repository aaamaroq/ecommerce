package application.ecommerce.service.product.application;

import application.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import application.ecommerce.service.product.domain.repository.ProductRepository;
import application.ecommerce.service.product.adapter.mapper.ProductMapper;
import application.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import application.ecommerce.service.product.domain.model.Product;
import application.ecommerce.service.product.adapter.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    private Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductResponseDTO getProductResponseById(Long id) throws ProductNotFoundException {
        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        return productMapper.toResponse(product);
    }

    public Long saveProduct(ProductKafkaCreateDTO.ProductData productData) {
        Product productEntity = productMapper.toEntity(productData);
        Product savedProduct = productRepository.save(productEntity);
        return savedProduct.getId();
    }


}
