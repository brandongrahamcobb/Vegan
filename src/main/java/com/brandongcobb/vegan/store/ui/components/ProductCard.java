package com.brandongcobb.vegan.store.ui.components;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.domain.ProductImage;
import com.brandongcobb.vegan.store.service.CartService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

public class ProductCard extends Div {

    private final Product product;
    private final CartService cartService;
    private final Consumer<Product> onAddToCart; // Callback to notify parent

    public ProductCard(Product product, CartService cartService, Consumer<Product> onAddToCart) {
        this.product = product;
        this.cartService = cartService;
        this.onAddToCart = onAddToCart;

        getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "1em")
                .set("padding", "1em")
                .set("border", "1px solid var(--lumo-border-color)")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("background", "var(--lumo-base-color)");

        ProductImage thumbnailImg = product.getThumbnail();
        String imageUrl = (thumbnailImg != null && thumbnailImg.getUrl() != null && !thumbnailImg.getUrl().isEmpty())
                ? thumbnailImg.getUrl()
                : "frontend/images/placeholder.png";

        Image thumbnail = new Image(imageUrl, product.getName());
        thumbnail.setWidth("100%");
        thumbnail.setHeight("150px");
        thumbnail.getStyle().set("object-fit", "cover"); // Ensure image covers the area
        thumbnail.addClickListener(e -> UI.getCurrent().navigate("product/" + product.getId()));
        thumbnail.getStyle().set("cursor", "pointer");

        Span brand = new Span(product.getBrand());
        brand.getStyle().set("font-size", "0.9em").set("color", "var(--lumo-secondary-text-color)");

        Span name = new Span(product.getName());
        name.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Span price = new Span("$" + product.getPrice().setScale(2, RoundingMode.HALF_UP));
        price.getStyle().set("font-weight", "bold").set("color", "var(--lumo-primary-text-color)");

        Span stock = new Span(product.getStock() > 0 ? "In Stock" : "Out of Stock");
        stock.getStyle().set("color", product.getStock() > 0 ? "green" : "red");

        Button addToCart = new Button("Add to Cart", e -> {
            int currentQtyInCart = cartService.getCartItems().getOrDefault(product, 0);
            if (currentQtyInCart >= product.getStock()) {
                Notification.show("Not enough stock available", 3000, Notification.Position.MIDDLE);
                return;
            }
            cartService.addToCart(product, 1);
            Notification.show("1 x " + product.getName() + " added to cart", 2000, Notification.Position.MIDDLE);
            if (onAddToCart != null) {
                onAddToCart.accept(product); // Notify parent to refresh cart display
            }
        });
        addToCart.setEnabled(product.getStock() > 0);
        addToCart.setWidthFull();

        add(thumbnail, name, brand, price, stock, addToCart);
    }
}
