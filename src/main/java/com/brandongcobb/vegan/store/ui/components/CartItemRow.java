package com.brandongcobb.vegan.store.ui.components;

import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.ui.views.CheckoutView.CartItem; // Import the public static nested class
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.icon.VaadinIcon;
// Removed @Autowired as this component will not be managed by Spring
// Removed @Component as this component will not be managed by Spring

import java.util.function.Runnable;

// Removed @Component annotation
public class CartItemRow extends HorizontalLayout {

    private final CartService cartService;
    private final CartItem cartItem;
    private final Runnable refreshParentCallback; // Callback to refresh the parent view

    // Removed @Autowired from constructor as this component is not Spring-managed
    public CartItemRow(CartService cartService, CartItem cartItem, Runnable refreshParentCallback) {
        this.cartService = cartService;
        this.cartItem = cartItem;
        this.refreshParentCallback = refreshParentCallback;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setWidthFull();

        // Quantity field
        IntegerField quantityField = new IntegerField();
        quantityField.setValue(cartItem.getQuantity());
        quantityField.setMin(1);
        quantityField.setMax(cartItem.getProduct().getStock()); // Max quantity is product's stock
        quantityField.setStepButtonsVisible(true);
        quantityField.setWidth("80px");

        quantityField.addValueChangeListener(e -> {
            int newQty = e.getValue() != null ? e.getValue() : 1;
            if (newQty > cartItem.getProduct().getStock()) {
                Notification.show("Cannot add more than available stock (" + cartItem.getProduct().getStock() + ")", 3000, Notification.Position.MIDDLE);
                quantityField.setValue(cartItem.getProduct().getStock());
                newQty = cartItem.getProduct().getStock();
            } else if (newQty < 1) {
                quantityField.setValue(1);
                newQty = 1;
            }
            cartService.updateQuantity(cartItem.getProduct(), newQty);
            refreshParentCallback.run(); // Notify parent to refresh
        });

        // Remove button
        Button removeButton = new Button(VaadinIcon.TRASH.create(), e -> {
            cartService.removeFromCart(cartItem.getProduct());
            refreshParentCallback.run(); // Notify parent to refresh
            Notification.show(cartItem.getProductName() + " removed from cart.", 2000, Notification.Position.MIDDLE);
        });
        removeButton.addThemeNames("icon", "tertiary");

        add(quantityField, removeButton);
    }
}
