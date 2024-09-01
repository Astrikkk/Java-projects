package org.example.controllers.api;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.mapper.CategoryMapper;
import org.example.repo.CategoryRepository;
import org.example.service.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;

    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> create(@ModelAttribute CategoryCreateDTO dto) {
        try {
            var entity = categoryMapper.categoryEntityByCategoryCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());
            String fileName = storageService.saveImage(dto.getFile(), FileSaveFormat.WEBP);
            entity.setImage(fileName);
            categoryRepository.save(entity);
//            var result = categoryMapper.categoryItemDTO(entity);
            return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}