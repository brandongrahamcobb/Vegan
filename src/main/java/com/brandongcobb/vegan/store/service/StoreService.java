package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.CategoryRepository;
import com.brandongcobb.vegan.store.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.service.CartService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public StoreService(CategoryRepository categoryRepo,
                        ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    // —— Category operations —— //

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepo.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }

    // —— Product operations —— //

    public List<Product> listProducts() {
        return productRepo.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public void deleteProductById(Long id) {
        productRepo.deleteById(id);
    }
    
    public void addProductImage(Long productId, String imageUrl) {
        Product product = productRepo.findProductWithImagesById(productId).orElseThrow();
        product.addImage(imageUrl);
        productRepo.save(product);
    }
    
    @Transactional
    public Optional<Product> transactFindProductById(Long id) {
        return productRepo.findProductWithImagesById(id);
    }
    
    public List<Category> listRootCategories() {
        return categoryRepo.findByParentIsNull();
    }
    
    // subcats of a given parent
    public List<Category> findSubCategories(Long parentId) {
        return categoryRepo.findByParentId(parentId);
    }
    
    // all categories (flat)
    public List<Category> listAllCategories() {
        return categoryRepo.findAll();
    }
}

