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

/**
 * Service class for product business logic.
 * Handles product retrieval and persistence.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    private Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Retrieves a product by its ID and maps it to a response DTO.
     * 
     * @param id the product ID
     * @return the product response DTO
     * @throws ProductNotFoundException if no product is found with the given ID
     */
    public ProductResponseDTO getProductResponseById(Long id) throws ProductNotFoundException {
        Product product = findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        return productMapper.toResponse(product);
    }

    /**
     * Saves a new product from the provided data.
     * 
     * @param productData the data of the product to save
     * @return the ID of the newly saved product
     */
    public Long saveProduct(ProductKafkaCreateDTO.ProductData productData) {
        Product productEntity = productMapper.toEntity(productData);
        Product savedProduct = productRepository.save(productEntity);
        return savedProduct.getId();
    }


}
