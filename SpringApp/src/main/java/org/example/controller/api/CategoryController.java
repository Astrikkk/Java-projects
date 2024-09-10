package org.example.controller.api;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryItemDTO; // Assuming you have a CategoryItemDTO class for responses
import org.example.mapper.CategoryMapper;
import org.example.model.CategoryEntity;
import org.example.repo.CategoryRepository;
import org.example.service.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> create(@ModelAttribute CategoryCreateDTO dto) {
        try {
            CategoryEntity entity = categoryMapper.categoryEntityByCategoryCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());
            String fileName = storageService.saveImage(dto.getImage(), FileSaveFormat.WEBP);
            entity.setImage(fileName);
            categoryRepository.save(entity);
            return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryItemDTO> getCategoryById(@PathVariable("id") Integer id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            CategoryEntity categoryEntity = categoryOptional.get();
            CategoryItemDTO categoryDTO = categoryMapper.categoryItemDTO(categoryEntity);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryItemDTO>> getAllCategories() {
        try {
            List<CategoryEntity> categories = categoryRepository.findAll();
            List<CategoryItemDTO> categoryDTOs = categories.stream()
                    .map(categoryMapper::categoryItemDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
