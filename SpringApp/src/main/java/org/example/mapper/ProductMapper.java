package org.example.mapper;

import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.model.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductEntity productEntityByProductCreateDTO(ProductCreateDTO dto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(dto.getName());
        productEntity.setDescription(dto.getDescription());
        productEntity.setPrice(dto.getPrice());
        productEntity.setDiscount(dto.getDiscount());
        return productEntity;
    }

    public ProductItemDTO productItemDTO(ProductEntity entity) {
        ProductItemDTO productItemDTO = new ProductItemDTO();
        productItemDTO.setId(entity.getId());
        productItemDTO.setName(entity.getName());
        productItemDTO.setDescription(entity.getDescription());
        productItemDTO.setPrice(entity.getPrice());
        productItemDTO.setDiscount(entity.getDiscount());
        productItemDTO.setCreationTime(entity.getCreationTime());
        productItemDTO.setCategoryName(entity.getCategory().getName());
        return productItemDTO;
    }

    // Add this method to map List<ProductEntity> to List<ProductItemDTO>
    public List<ProductItemDTO> toDto(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(this::productItemDTO)
                .collect(Collectors.toList());
    }
}
