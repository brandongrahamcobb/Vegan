package com.brandongcobb.vegan.store.ui.components;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.CartService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.List;
import java.util.function.Consumer;

public class CategoryCarousel extends Div {

    public CategoryCarousel(Category category, List<Product> products, CartService cartService, Runnable cartRefreshCallback) {
        H2 categoryTitle = new H2(category.getName());
        add(categoryTitle);

        HorizontalLayout carousel = new HorizontalLayout();
        carousel.setSpacing(true);
        carousel.getStyle().set("overflow-x", "auto"); // Enable horizontal scrolling

        products.forEach(product -> {
            ProductCard card = new ProductCard(product, cartService, p -> cartRefreshCallback.run());
            carousel.add(card);
        });

        add(carousel);
    }
}
