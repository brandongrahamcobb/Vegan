package com.brandongcobb.vegan.store.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    Long categoryId,
    String categoryName,
    String imageUrl,              // Primary image (thumbnail)
    List<String> imageUrls,       // Full gallery
    String brand,
    String dimensions,
    Double weight,
    String tags,
    String seoTitle,
    String seoKeywords,
    String seoDescription,
    boolean active,
    LocalDate dateAdded
) {}
