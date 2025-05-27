package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.domain.ProductImage;
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
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.annotation.Scope;
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

    public List<Category> getCategoryList() {
        return categoryRepo.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepo.getCategoryById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }

    // —— Product operations —— //

    public List<Product> getProductList() {
        return productRepo.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public void deleteProductById(Long id) {
        productRepo.deleteById(id);
    }
    
    public void addImageByIdAndUrl(Long productId, String imageUrl) {
        Optional<Product> opt = getProductByProductId(productId);
        if (opt.isEmpty()) return;
        
        Product product = opt.get();
        product.addImage(imageUrl); // This assumes your Product class has a method called addImage(String)
        saveProduct(product);
    }    
    
    
    @Transactional
    public Optional<Product> getProductByProductId(Long id) {
        return productRepo.getProductByProductId(id);
    }
    
    public List<Category> getRootCategoriesList() {
        return categoryRepo.getCategoryParentIsNull();
    }
    
    // subcats of a given parent
    public List<Category> getSubCategoriesById(Long parentId) {
        return categoryRepo.getCategoryByParentId(parentId);
    }
    
    // all categories (flat)
    public List<Category> getCategoriesList() {
        return categoryRepo.findAll();
    }

     public void addImage(Long productId, String imageUrl) {
        Optional<Product> opt = getProductByProductId(productId);
        if (opt.isEmpty()) return;
        
        Product product = opt.get();
        product.addImage(imageUrl); // This assumes your Product class has a method called addImage(String)
        saveProduct(product);
    }    
}
