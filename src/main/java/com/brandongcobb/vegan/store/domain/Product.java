package com.brandongcobb.vegan.store.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductImage> images = new ArrayList<>();

    public List<ProductImage> getImages() {
        return images;
    }

    public void addImage(String imageUrl) {
        ProductImage image = new ProductImage();
        image.setUrl(imageUrl);
        image.setProduct(this);
        this.images.add(image);
    }

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // --- New extended fields ---
    @Column(length = 150)
    private String brand;

    @Column(length = 100)
    private String dimensions;  // e.g. "10x5x3 cm"

    private Double weight;      // in kg

    @Column(length = 500)
    private String tags;        // comma separated tags

    @Column(name = "seo_title", length = 150)
    private String seoTitle;

    @Column(name = "seo_keywords", length = 500)
    private String seoKeywords;

    @Column(name = "seo_description", length = 1000)
    private String seoDescription;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "date_added")
    private LocalDate dateAdded;
    
    // --- Constructors ---

    public Product() { }

    public Product(String name, String description, BigDecimal price, Integer stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.active = true;
        this.dateAdded = LocalDate.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    // No setter for id; managed by JPA

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
 
    public ProductImage getThumbnail() {
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }

    public List<String> getImageUrls() {
        return images == null ? List.of() :
               images.stream().map(ProductImage::getUrl).toList();
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.2f) [%d in stock]", name, price, stock);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id); // or whatever unique identifier you use
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
