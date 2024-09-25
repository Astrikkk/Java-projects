package org.example.controller.api;

import lombok.AllArgsConstructor;
import org.example.dto.product.ProductCreateDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.mapper.ProductMapper;
import org.example.model.ProductEntity;
import org.example.model.ProductImageEntity;
import org.example.repo.ProductRepository;
import org.example.service.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StorageService storageService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createProduct(@ModelAttribute ProductCreateDTO dto) {
        try {
            ProductEntity entity = productMapper.productEntityByProductCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());

            // Save associated images
            dto.getImages().forEach(image -> {
                try {
                    String fileName = storageService.saveImage(image, FileSaveFormat.WEBP);
                    entity.getProductImages().add(new ProductImageEntity(null, fileName, 0, new Date(), false, entity));
                } catch (IOException e) {
                    e.printStackTrace(); // Log error
                }
            });

            productRepository.save(entity);
            return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductItemDTO> getProductById(@PathVariable("id") Long id) {
        Optional<ProductEntity> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();
            ProductItemDTO productDTO = productMapper.productItemDTO(productEntity);
            productDTO.setImages(productEntity.getProductImages().stream()
                    .map(image -> "http://localhost:8888/images/" + image.getName())
                    .collect(Collectors.toList()));
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ProductItemDTO>> getAllProducts() {
        var products = productMapper.toDto(productRepository.findAll());
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws IOException {
        Optional<ProductEntity> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            ProductEntity entity = productOptional.get();
            entity.getProductImages().forEach(image -> {
                try {
                    storageService.deleteImage(image.getName());
                } catch (IOException e) {
                    e.printStackTrace(); // Log error
                }
            });
            productRepository.delete(entity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
