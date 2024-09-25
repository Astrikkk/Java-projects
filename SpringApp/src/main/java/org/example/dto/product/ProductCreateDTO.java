package org.example.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateDTO {
    private String name;
    private String description;
    private double price;
    private double discount;
    private Long categoryId;  // Assuming you need to link this with an existing Category
    private List<MultipartFile> images;  // List of images uploaded
}
