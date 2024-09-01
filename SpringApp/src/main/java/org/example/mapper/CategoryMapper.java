package org.example.mapper;

import org.example.dto.category.CategoryCreateDTO;
import org.example.model.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "image", ignore = true)
    CategoryEntity categoryEntityByCategoryCreateDTO(CategoryCreateDTO category);
}