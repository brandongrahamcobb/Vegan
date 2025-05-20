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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.brandongcobb.vegan.store.ui.base.*;
import org.springframework.stereotype.Component;


@Route("product/:productId")
@PageTitle("Product Details | Vegan Store")
public class ProductDetailView extends Composite<VerticalLayout> {

    private final StoreService service;
    private final CartService cartService;
    private Product product;

    @Autowired
    public ProductDetailView(StoreService service, CartService cartService) {
        this.service = service;
        this.cartService = cartService;
        getContent().setSizeFull();
        getContent().setPadding(true);
    }

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

    public void buildLayout() {
        getContent().removeAll();

        // --- Main image & Vertical Thumbs ---
        // Imagine ProductGallery can render images as VERTICAL thumbnails
        // (if not, adapt its code or do a simple Column of thumbnail Images)
        VerticalLayout galleryCol = new VerticalLayout();
        galleryCol.setPadding(false);
        galleryCol.setSpacing(false);
        galleryCol.setAlignItems(Alignment.CENTER);

        // Vertical list of thumbs on the left,
        // main image to the right of thumbs.
        HorizontalLayout imageBlock = new HorizontalLayout();
        imageBlock.setDefaultVerticalComponentAlignment(Alignment.START);

        VerticalLayout thumbs = new VerticalLayout();
        thumbs.setSpacing(false);
        Image mainImage = new Image(safeImageUrl(product.getImageUrl()), product.getName());
        product.getImageUrls().forEach(url -> {
            Image thumb = new Image(url, "Thumbnail");
            thumb.setWidth("50px");
            thumb.setHeight("50px");
            thumb.getStyle().set("cursor", "pointer")
                .set("border", "1px solid #ddd")
                .set("margin-bottom", "6px");
            thumb.addClickListener(e -> mainImage.setSrc(safeImageUrl(url)));
            thumbs.add(thumb);
        });

       
        mainImage.setWidth("360px");
        mainImage.getStyle().set("border-radius", "8px")
            .set("box-shadow", "0 4px 16px #eee")
            .set("margin", "0 6px");

        imageBlock.add(thumbs, mainImage);

        // --- Center: Details ---
        VerticalLayout detailsCol = new VerticalLayout();
        detailsCol.setPadding(false);
        detailsCol.setSpacing(true);
        detailsCol.setWidth("420px");

        H2 name = new H2(product.getName());
        name.getStyle().set("font-size", "2em");

        Paragraph category = new Paragraph("Category: " +
            (product.getCategory() != null ? product.getCategory().getName() : "N/A"));
        category.getStyle().set("color", "#888");

        Paragraph price = new Paragraph("$" + product.getPrice().setScale(2, RoundingMode.HALF_UP));
        price.getStyle().set("font-size", "1.6em").set("color", "#B12704").set("font-weight", "bold");

        Paragraph stock = new Paragraph(product.getStock() > 0 ?
            "In Stock" : "Out of Stock");
        stock.getStyle().set("color", product.getStock() > 0 ? "green" : "red");

        Paragraph desc = new Paragraph(product.getDescription());

        detailsCol.add(name, category, price, stock, desc);

        // --- Right: Cart box ---
        VerticalLayout cartCol = new VerticalLayout();
        cartCol.getStyle()
            .set("background", "#fafafa")
            .set("border", "1px solid #ebecee")
            .set("border-radius", "10px")
            .set("padding", "1.2em")
            .set("margin-top", "0");
        cartCol.setAlignItems(Alignment.STRETCH);

        Paragraph cartPrice = new Paragraph("$" + product.getPrice().setScale(2, RoundingMode.HALF_UP));
        cartPrice.getStyle().set("font-size", "1.25em").set("color", "#B12704").set("font-weight", "bold");

        Button addToCart = new Button("Add to Cart", e -> {
            cartService.addToCart(product, 1);
            Notification.show(product.getName() + " added to cart");
        });
        addToCart.getStyle().set("background", "#FFD814").set("font-size", "1.1em");
        addToCart.setWidthFull();

        cartCol.add(cartPrice, stock, addToCart);

        // --- Top-align all columns, set min widths, add spacing ---
        HorizontalLayout main = new HorizontalLayout(imageBlock, detailsCol, cartCol);
        main.setWidthFull();
        main.setSpacing(true);
        main.setDefaultVerticalComponentAlignment(Alignment.START);

        imageBlock.getStyle().set("margin-right", "36px");
        detailsCol.getStyle().set("margin-right", "36px");
        Button back = new Button("← Back", e -> UI.getCurrent().navigate("store"));
        getContent().add(main);
        getContent().setHorizontalComponentAlignment(Alignment.CENTER, main);
    }
    
    private static String safeImageUrl(String url) {
        return (url != null && !url.trim().isEmpty()) ? url : "frontend/images/placeholder.png";
    }
    
    
}
