//
//  ProductDetailView.java
//  
//
//  Created by Brandon Cobb on 5/18/25.
//

package com.brandongcobb.vegan.store.ui;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.*;
import com.brandongcobb.vegan.store.ui.components.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.components.ProductGallery;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Route("product/:productId")
@PageTitle("Product Details | Vegan Store")
public class ProductDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService service;
    private final CartService cartService;
    private Product product;

    @Autowired
    public ProductDetailView(StoreService service, CartService cartService) {
        this.service = service;
        this.cartService = cartService;
        setSizeFull();
        setPadding(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Expect product ID as a route parameter
        Optional<String> param = event.getRouteParameters().get("productId");
        if (!param.isPresent()) { return; }
        Long productId = Long.valueOf(param.get());
        List<String> segments = event.getLocation().getSegments();
        if (segments.size() < 2) {
            event.rerouteTo("store");  // fallback if no ID provided
            return;
        }

        try {
        //    Long productId = Long.valueOf(segments.get(1));
            product = service.transactFindProductById(productId).orElse(null);
            if (product == null) {
                Notification.show("Product not found");
                event.rerouteTo("store");
                return;
            }
            buildLayout();
        } catch (NumberFormatException e) {
            Notification.show("Invalid product ID");
            event.rerouteTo("store");
        }
    }

    private void buildLayout() {
        removeAll();

        H2 name = new H2(product.getName());

        Paragraph description = new Paragraph(product.getDescription());

        Paragraph price = new Paragraph("Price: $" + product.getPrice().setScale(2, RoundingMode.HALF_UP));

        Paragraph stock = new Paragraph("Stock: " + product.getStock());

        Paragraph category = new Paragraph("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "N/A"));

        // Main image
        Image mainImage = new Image(product.getImageUrl() != null ? product.getImageUrl() : "frontend/images/placeholder.png", product.getName());
        mainImage.setWidth("300px");
        mainImage.getStyle().set("border-radius", "10px");

        ProductGallery gallery = new ProductGallery(product.getImageUrls());
        Button addToCart = new Button("Add to Cart", e -> {
            cartService.addToCart(product, 1);
            Notification.show(product.getName() + " added to cart");
        });
        add(name, gallery, mainImage, price, stock, category, description, addToCart);

        setAlignItems(Alignment.CENTER);
    }
}
