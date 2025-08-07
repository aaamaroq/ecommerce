package application.ecommerce.service.product.adapter.mapper;

import application.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import application.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import application.ecommerce.service.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponseDTO toResponse(Product product);

    Product toEntity(ProductKafkaCreateDTO.ProductData ProductData);
}
