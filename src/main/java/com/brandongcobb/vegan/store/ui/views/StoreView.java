package com.brandongcobb.vegan.store.ui.views;

import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.layouts.*;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "store", layout = MainLayout.class)
@PageTitle("Store | The Vyrtuous Project")
@PermitAll
public class StoreView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final StoreService storeService;
    private final CartService cartService;
    private final OrderService orderService;
    private final VeganRepository veganRepo;

    private final VerticalLayout catalog = new VerticalLayout();
    private final Div cartDropdown = new Div();
    private final Button cartBtn = new Button(com.vaadin.flow.component.icon.VaadinIcon.CART.create());
    private final Map<Product, Integer> cartSnapshot = new LinkedHashMap<>();

    @Autowired
    public StoreView(StoreService storeService, CartService cartService,
                     OrderService orderService, VeganRepository veganRepo) {
        this.storeService = storeService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.veganRepo = veganRepo;

        VerticalLayout root = getContent();
        root.setSizeFull();
        root.setPadding(false);
        root.setSpacing(false);

        // --- Cart dropdown setup ---
        cartDropdown.addClassName("cart-dropdown");
        cartDropdown.getStyle()
                .set("position", "absolute")
                .set("top", "100%")
                .set("left", "0")
                .set("background", "white")
                .set("border", "1px solid #bbb")
                .set("padding", "1em")
                .set("min-width", "200px")
                .set("z-index", "1000")
                .set("display", "none");

        Div cartContainer = new Div(cartBtn, cartDropdown);
        cartContainer.getStyle()
                .set("position", "fixed")
                .set("top", "16px")
                .set("right", "16px")
                .set("z-index", "10000");

        cartContainer.getElement().addEventListener("mouseenter", e -> {
            cartDropdown.getStyle().set("display", "block");
        });
        cartContainer.getElement().addEventListener("mouseleave", e -> {
            cartDropdown.getStyle().set("display", "none");
        });

        cartBtn.addClickListener(e -> UI.getCurrent().navigate("checkout"));

        // --- Catalog population ---
        catalog.addClassName("product-catalog");
        catalog.setSizeFull();

        // --- Bot assistant ---
        Image botIcon = new Image("/images/assistant.svg", "AI Assistant");
        botIcon.getStyle()
                .set("position", "fixed")
                .set("bottom", "24px")
                .set("right", "24px")
                .set("width", "64px")
                .set("cursor", "pointer")
                .set("z-index", "10000");

        Div botPopup = new Div();
        botPopup.getStyle()
                .set("position", "fixed")
                .set("bottom", "100px")
                .set("right", "24px")
                .set("width", "300px")
                .set("background", "rgba(255,255,255,0.9)")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 16px rgba(0,0,0,0.2)")
                .set("padding", "12px")
                .set("display", "none")
                .set("z-index", "10001");

        VerticalLayout messages = new VerticalLayout();
        TextField userInput = new TextField();
        userInput.setPlaceholder("Speak into the void...");
        Button send = new Button("Send");

        send.addClickListener(click -> {
            String input = userInput.getValue();
            if (!input.isBlank()) {
                messages.add(new Span("👽 " + input));
                userInput.clear();
                Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
                // TODO: Integrate AI backend here
            }
        });

        botPopup.add(messages, userInput, send);
        botIcon.addClickListener(e -> {
            boolean visible = "block".equals(botPopup.getStyle().get("display"));
            botPopup.getStyle().set("display", visible ? "none" : "block");
        });

        // --- Add components to view ---
        root.add(cartContainer, catalog, botIcon, botPopup);
    }

    private void refreshCatalog(String filter) {
        catalog.removeAll();
        String ctx = com.vaadin.flow.server.VaadinServletRequest.getCurrent().getContextPath();

        List<Product> prods = storeService.findAllProducts().stream()
                .filter(p -> filter == null || filter.isBlank() || p.getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

        prods.forEach(p -> {
            VerticalLayout card = new VerticalLayout();
            card.addClassName("product-card");

            String img = (p.getImageUrl() == null) ? ctx + "/images/placeholder.png" : ctx + p.getImageUrl();
            Image image = new Image(img, p.getName());
            image.setWidth("100%");
            image.addClickListener(ev -> UI.getCurrent().navigate("product/" + p.getId()));

            Span name = new Span(p.getName());
            Span price = new Span("$" + p.getPrice());

            Button addToCart = new Button("Add", ev -> {
                cartService.addToCart(p, 1);
                refreshCartGrid();
            });

            card.add(image, name, price, addToCart);
            card.setWidth("200px");
            catalog.add(card);
        });
    }

    private void refreshCartGrid() {
        cartSnapshot.clear();
        cartSnapshot.putAll(cartService.getCartItems());
        cartDropdown.removeAll();

        if (cartSnapshot.isEmpty()) {
            cartDropdown.add(new Span("Cart is empty"));
        } else {
            Grid<CartItem> dropdownCart = new Grid<>(CartItem.class, false);
            dropdownCart.setAllRowsVisible(true);

            dropdownCart.addColumn(ci -> ci.product.getName()).setHeader("Product");
            dropdownCart.addColumn(ci -> ci.quantity).setHeader("Qty");
            dropdownCart.addComponentColumn(ci -> {
                Button rm = new Button("×", e -> {
                    cartService.removeFromCart(ci.product);
                    refreshCartGrid();
                });
                return rm;
            }).setHeader("Remove");

            dropdownCart.setItems(
                    cartSnapshot.entrySet().stream()
                            .map(e -> new CartItem(e.getKey(), e.getValue()))
                            .collect(Collectors.toList())
            );

            cartDropdown.add(dropdownCart);
            cartDropdown.add(new Button("Go to Checkout", e -> UI.getCurrent().navigate("checkout")));
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            veganRepo.findByEmail(email).ifPresent(v -> {
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
                // Avatar setup placeholder
                // Example: update some session-bound component with user info
            });
        }

        refreshCatalog(null);
        refreshCartGrid();
    }

    private record CartItem(Product product, int quantity) {}
}
