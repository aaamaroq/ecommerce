package com.ecommerce.service.notification.product.adapter.mapper;

import com.ecommerce.service.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.service.notification.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponse toResponse(Product product);
}
