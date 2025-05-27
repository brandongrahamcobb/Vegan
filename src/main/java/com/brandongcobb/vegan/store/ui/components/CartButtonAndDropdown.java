package com.brandongcobb.vegan.store.ui.components;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.ui.views.CheckoutView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CartButtonAndDropdown extends Div {

    private final CartService cartService;

    private final Button cartBtn;
    private final VerticalLayout cartDropdown;

    @Autowired
    public CartButtonAndDropdown(CartService cartService) {
        this.cartService = cartService;

        addClassName("cart-button-and-dropdown-container");
        getStyle()
                .set("position", "fixed")
                .set("bottom", "16px")
                .set("right", "16px")
                .set("z-index", "10000");

        // Cart dropdown setup
        cartDropdown = new VerticalLayout();
        cartDropdown.addClassName("cart-dropdown");
        cartDropdown.getStyle()
                .set("position", "absolute")
                .set("bottom", "100%")
                .set("right", "0")
                .set("background", "white")
                .set("border", "1px solid #bbb")
                .set("padding", "1em")
                .set("min-width", "200px")
                .set("z-index", "1000")
                .set("display", "none"); // Initially hidden

        // Cart button
        cartBtn = new Button(VaadinIcon.CART.create());
        cartBtn.addClickListener(e -> UI.getCurrent().navigate("checkout"));

        // Hover behavior for dropdown
        getElement().addEventListener("mouseenter", e -> cartDropdown.getStyle().set("display", "block"));
        getElement().addEventListener("mouseleave", e -> cartDropdown.getStyle().set("display", "none"));

        add(cartBtn, cartDropdown);

        refresh(); // Initial refresh
    }

    public void refresh() {
        cartDropdown.removeAll();
        Map<Product, Integer> items = cartService.getCartItems();

        if (items.isEmpty()) {
            cartDropdown.add(new Span("Cart is empty"));
        } else {
            Grid<CartItemDisplay> dropdownCart = new Grid<>(CartItemDisplay.class, false);
            dropdownCart.setAllRowsVisible(true);

            dropdownCart.addColumn(ci -> ci.product.getName()).setHeader("Product");
            dropdownCart.addColumn(ci -> ci.quantity).setHeader("Qty");
            dropdownCart.addComponentColumn(ci -> new Button("×", e -> {
                cartService.removeFromCart(ci.product);
                refresh(); // Refresh dropdown after removal
            })).setHeader("Remove");

            dropdownCart.setItems(
                    items.entrySet().stream().map(e -> new CartItemDisplay(e.getKey(), e.getValue())).toList()
            );

            cartDropdown.add(dropdownCart);
            cartDropdown.add(new Button("Go to Checkout", e -> UI.getCurrent().navigate("checkout")));
        }
    }

    // Helper record for Grid display within the dropdown
    private record CartItemDisplay(Product product, int quantity) {}
}
