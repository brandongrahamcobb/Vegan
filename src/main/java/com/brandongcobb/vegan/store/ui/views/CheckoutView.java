//

//
//
//  Created by Brandon Cobb on 5/20/25.
//

package com.brandongcobb.vegan.store.ui.views;
import com.vaadin.flow.component.Composite;
//  CheckoutView.java
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.brandongcobb.vegan.store.ui.base.*;
import com.brandongcobb.vegan.store.ui.layouts.*;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.api.dto.OrderLineRequest;
import com.brandongcobb.vegan.store.api.dto.PlaceOrderRequest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.brandongcobb.vegan.store.ui.components.CartItemRow; // Import the new component

@Route(value = "checkout", layout = MainLayout.class)
@PageTitle("Checkout | The Vyrtuous Project")
public class CheckoutView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final CartService cartService;
    private final OrderService orderService;

    private Grid<CartItem> cartGrid;
    private Span totalAmountSpan;
    private Button placeOrderButton;
    private Button continueShoppingButton;
    private Button emptyCartButton;

    @Autowired
    public CheckoutView(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;

        getContent().setSizeFull();
        getContent().setPadding(true);
        getContent().setSpacing(true);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        H2 header = new H2("Your Shopping Cart");
        getContent().add(header);

        // Cart Grid
        cartGrid = new Grid<>(CartItem.class, false);
        cartGrid.setAllRowsVisible(true);
        cartGrid.setWidth("80%");
        cartGrid.setMaxWidth("900px");

        cartGrid.addColumn(CartItem::getProductName).setHeader("Product");
        cartGrid.addColumn(CartItem::getUnitPrice).setHeader("Unit Price");
        // Use the new CartItemRow component for quantity and remove actions
        cartGrid.addComponentColumn(item -> new CartItemRow(cartService, item, this::refreshCartDisplay)).setHeader("Quantity / Actions");
        cartGrid.addColumn(CartItem::getSubtotal).setHeader("Subtotal");
        // Removed the separate remove button column as it's now part of CartItemRow

        getContent().add(cartGrid);

        // Total amount
        totalAmountSpan = new Span("Total: $0.00");
        totalAmountSpan.getStyle().set("font-weight", "bold").set("font-size", "1.5em");
        getContent().add(totalAmountSpan);
        getContent().setAlignSelf(FlexComponent.Alignment.END, totalAmountSpan); // Align total to the right

        // Action buttons
        placeOrderButton = new Button("Place Order", VaadinIcon.CHECK.create(), e -> placeOrder());
        placeOrderButton.addThemeNames("primary", "large");

        continueShoppingButton = new Button("Continue Shopping", VaadinIcon.ARROW_LEFT.create(), e -> UI.getCurrent().navigate("store"));
        emptyCartButton = new Button("Empty Cart", VaadinIcon.TRASH.create(), e -> emptyCart());

        HorizontalLayout buttonsLayout = new HorizontalLayout(continueShoppingButton, emptyCartButton, placeOrderButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonsLayout.setWidth("80%");
        buttonsLayout.setMaxWidth("900px");
        getContent().add(buttonsLayout);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, buttonsLayout); // Center buttons layout

        refreshCartDisplay();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Ensure cart display is up-to-date when entering the view
        refreshCartDisplay();
        if (cartService.isEmpty()) {
            Notification.show("Your cart is empty. Add some products first!", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("store"); // Redirect to store if cart is empty
        }
    }

    // Removed createQuantityField and createRemoveButton as they are now in CartItemRow

    private void refreshCartDisplay() {
        List<CartItem> items = cartService.getCartItems().entrySet().stream()
                .map(entry -> new CartItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        cartGrid.setItems(items);

        // Use the new getTotalPrice() method from CartService
        BigDecimal total = cartService.getTotalPrice();
        totalAmountSpan.setText("Total: $" + total.setScale(2, RoundingMode.HALF_UP));

        // Enable/disable place order button based on cart content
        placeOrderButton.setEnabled(!cartService.isEmpty());
        emptyCartButton.setEnabled(!cartService.isEmpty());
    }

    private void placeOrder() {
        Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
        if (userId == null) {
            Notification.show("You must be logged in to place an order.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("login"); // Redirect to login
            return;
        }

        if (cartService.isEmpty()) {
            Notification.show("Your cart is empty. Nothing to order!", 3000, Notification.Position.MIDDLE);
            return;
        }

        List<OrderLineRequest> orderLines = cartService.getCartItems().entrySet().stream()
                .map(entry -> new OrderLineRequest(entry.getKey().getId(), entry.getValue()))
                .collect(Collectors.toList());

        PlaceOrderRequest request = new PlaceOrderRequest(userId, orderLines);

        try {
            orderService.placeOrder(request);
            cartService.clear(); // Clear cart after successful order
            Notification.show("Order placed successfully!", 3000, Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate("orders"); // Navigate to user's orders page
        } catch (ResponseStatusException e) {
            Notification.show("Failed to place order: " + e.getReason(), 5000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("An unexpected error occurred: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void emptyCart() {
        cartService.clear();
        refreshCartDisplay();
        Notification.show("Cart emptied.", 2000, Notification.Position.MIDDLE);
    }

    // Helper record for Grid display - Made public static for CartItemRow component
    public static class CartItem {
        private final Product product;
        private final int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public String getProductName() {
            return product.getName();
        }

        public BigDecimal getUnitPrice() {
            return product.getPrice().setScale(2, RoundingMode.HALF_UP);
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getSubtotal() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
