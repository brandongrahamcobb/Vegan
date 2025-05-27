//
//  ProductDetailView.java
//  
//
//  Created by Brandon Cobb on 5/18/25.
//

package com.brandongcobb.vegan.store.ui.views;
import com.brandongcobb.vegan.store.ui.views.*;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.*;
import com.brandongcobb.vegan.store.ui.layouts.*;
import com.brandongcobb.vegan.store.ui.components.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.Composite;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.brandongcobb.vegan.store.ui.base.*;
import org.springframework.stereotype.Component;


@Route(value = "product/:productId", layout = MainLayout.class)
@PageTitle("Product Details | Vegan Store")
public class ProductDetailView extends Composite<VerticalLayout> implements BeforeEnterObserver { // Implemented BeforeEnterObserver

    private final StoreService service;
    private final CartService cartService;
    private Product product;
    private IntegerField quantityField;

    @Autowired
    public ProductDetailView(StoreService service, CartService cartService) {
        this.service = service;
        this.cartService = cartService;
        getContent().setSizeFull();
        getContent().setPadding(true);
        getContent().setAlignItems(Alignment.CENTER); // Center content horizontally
    }

    @Override // Implemented BeforeEnterObserver
    public void beforeEnter(BeforeEnterEvent event) {
        // Expect product ID as a route parameter
        Optional<String> param = event.getRouteParameters().get("productId");
        if (!param.isPresent()) {
            event.rerouteTo("store");  // fallback if no ID provided
            return;
        }
        Long productId = Long.valueOf(param.get());

        try {
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

    public void buildLayout() {
        getContent().removeAll();

        // Back button
        Button backButton = new Button("← Back to Store", e -> UI.getCurrent().navigate("store"));
        backButton.getStyle().set("margin-bottom", "1em");
        getContent().add(backButton);
        getContent().setAlignSelf(Alignment.START, backButton); // Align back button to the start

        // Main content layout
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setWidthFull();
        mainContent.setSpacing(true);
        mainContent.setDefaultVerticalComponentAlignment(Alignment.START);
        mainContent.setMaxWidth("1200px"); // Constrain max width for better readability

        // --- Left: Image Gallery ---
        VerticalLayout galleryCol = new VerticalLayout();
        galleryCol.setPadding(false);
        galleryCol.setSpacing(false);
        galleryCol.setAlignItems(Alignment.CENTER);
        galleryCol.setWidth("40%"); // Allocate more space for the gallery

        ProductGallery productGallery = new ProductGallery(product.getImageUrls());
        galleryCol.add(productGallery);

        // --- Center: Product Details ---
        VerticalLayout detailsCol = new VerticalLayout();
        detailsCol.setPadding(false);
        detailsCol.setSpacing(true);
        detailsCol.setWidth("40%"); // Allocate space for details

        H2 name = new H2(product.getName());
        name.getStyle().set("font-size", "2em").set("margin-top", "0");

        Paragraph category = new Paragraph("Category: " +
            (product.getCategory() != null ? product.getCategory().getFullPath() : "N/A"));
        category.getStyle().set("color", "var(--lumo-secondary-text-color)");

        Paragraph price = new Paragraph("$" + product.getPrice().setScale(2, RoundingMode.HALF_UP));
        price.getStyle().set("font-size", "1.6em").set("color", "var(--lumo-primary-text-color)").set("font-weight", "bold");

        Paragraph stock = new Paragraph(product.getStock() > 0 ?
            "In Stock (" + product.getStock() + " available)" : "Out of Stock");
        stock.getStyle().set("color", product.getStock() > 0 ? "green" : "red");

        Paragraph desc = new Paragraph(product.getDescription());
        desc.getStyle().set("margin-top", "1em");

        // Extended details
        Div extendedDetails = new Div();
        extendedDetails.add(new H4("Product Information"));
        if (product.getBrand() != null && !product.getBrand().isEmpty()) {
            extendedDetails.add(new Paragraph("Brand: " + product.getBrand()));
        }
        if (product.getDimensions() != null && !product.getDimensions().isEmpty()) {
            extendedDetails.add(new Paragraph("Dimensions: " + product.getDimensions()));
        }
        if (product.getWeight() != null) {
            extendedDetails.add(new Paragraph("Weight: " + product.getWeight() + " kg"));
        }
        if (product.getTags() != null && !product.getTags().isEmpty()) {
            extendedDetails.add(new Paragraph("Tags: " + product.getTags()));
        }
        extendedDetails.getStyle().set("margin-top", "1.5em").set("border-top", "1px solid var(--lumo-contrast-10pct)");
        extendedDetails.getStyle().set("padding-top", "1em"); // Corrected line


        detailsCol.add(name, category, price, stock, desc, extendedDetails);

        // --- Right: Cart box ---
        VerticalLayout cartCol = new VerticalLayout();
        cartCol.getStyle()
            .set("background", "var(--lumo-contrast-5pct)")
            .set("border", "1px solid var(--lumo-contrast-10pct)")
            .set("border-radius", "10px")
            .set("padding", "1.2em")
            .set("margin-top", "0");
        cartCol.setAlignItems(Alignment.STRETCH);
        cartCol.setWidth("20%"); // Allocate space for cart box

        Paragraph cartPrice = new Paragraph("$" + product.getPrice().setScale(2, RoundingMode.HALF_UP));
        cartPrice.getStyle().set("font-size", "1.25em").set("color", "var(--lumo-primary-text-color)").set("font-weight", "bold");

        quantityField = new IntegerField("Quantity");
        quantityField.setValue(1);
        quantityField.setStepButtonsVisible(true);
        quantityField.setMin(1);
        quantityField.setMax(product.getStock());
        quantityField.addValueChangeListener(e -> {
            if (quantityField.getValue() == null || quantityField.getValue() < 1) {
                quantityField.setValue(1);
            } else if (quantityField.getValue() > product.getStock()) {
                quantityField.setValue(product.getStock());
            }
        });
        quantityField.setValueChangeMode(ValueChangeMode.EAGER);
        quantityField.setWidthFull();

        Button addToCart = new Button("Add to Cart", e -> {
            int qty = quantityField.getValue() != null ? quantityField.getValue() : 1;
            if (qty > 0 && qty <= product.getStock()) {
                cartService.addToCart(product, qty);
                Notification.show(qty + " x " + product.getName() + " added to cart", 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show("Invalid quantity or insufficient stock.", 3000, Notification.Position.MIDDLE);
            }
        });
        addToCart.getStyle().set("background", "var(--lumo-primary-color)").set("color", "white").set("font-size", "1.1em");
        addToCart.setWidthFull();
        addToCart.setEnabled(product.getStock() > 0); // Disable if out of stock

        cartCol.add(cartPrice, quantityField, addToCart, stock); // Reordered stock to be near quantity/add to cart

        mainContent.add(galleryCol, detailsCol, cartCol);
        getContent().add(mainContent);
    }
    
    private static String safeImageUrl(String url) {
        return (url != null && !url.trim().isEmpty()) ? url : "frontend/images/placeholder.png";
    }
}
