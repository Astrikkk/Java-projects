package org.example.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private LocalDateTime creationTime;
    private String categoryName;
    private List<String> images;  // URLs or paths to images
}
