package com.brandongcobb.vegan.store.ui.views;
import com.brandongcobb.vegan.store.ui.views.*;
import com.brandongcobb.vegan.store.api.dto.OrderLineRequest;
import com.brandongcobb.vegan.store.api.dto.PlaceOrderRequest;
import com.brandongcobb.vegan.store.api.dto.OrderResponse;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.CustomerRepository;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.button.Button;
import java.util.LinkedHashMap;
import java.util.Map;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.ui.layouts.*;
import com.brandongcobb.vegan.store.api.dto.OrderLineRequest;
import com.brandongcobb.vegan.store.api.dto.PlaceOrderRequest;
import com.brandongcobb.vegan.store.api.dto.OrderResponse;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.CustomerRepository;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.service.CartService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.LinkedHashMap;
import java.util.Map;
import com.vaadin.flow.component.dependency.CssImport;

@CssImport("./styles/store-styles.css")
@Route(value = "store", layout = StoreLayout.class)
@PageTitle("Store | Vegan Store")
public class StoreView extends VerticalLayout implements View {

    private final StoreService storeService;
    private final OrderService orderService;
    private final CartService cartService;
    private final CustomerRepository customerRepo;

    private final Div catalog     = new Div();
    private final Grid<CartItem> cartGrid    = new Grid<>(CartItem.class, false);
    private final Button checkout           = new Button("Checkout");
    private final Button clearCart          = new Button("Clear Cart");

    private final Map<Product,Integer> cart = new LinkedHashMap<>();

    public StoreView(StoreService storeService,
                     OrderService orderService,
                     CustomerRepository customerRepo,
                     CartService cartService) {
        this.storeService  = storeService;
        this.cartService = cartService;
        this.orderService  = orderService;
        this.customerRepo  = customerRepo;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // apply the CSS grid class
        catalog.addClassName("product-catalog");
        configureCartGrid();
        buildProductCatalog();
        buildLayout();
        updateCartGrid();
        
    }

    private void buildProductCatalog() {
        catalog.removeAll();

        // Prepend context path so "/uploads/..." URLs resolve
        String context = VaadinServletRequest.getCurrent()
                            .getHttpServletRequest()
                            .getContextPath();

        for (Product p : storeService.listProducts()) {
            Div card = new Div();
            card.addClassName("product-card");

            String raw = p.getImageUrl();
            String src = (raw != null && !raw.isBlank())
                         ? context + raw
                         : context + "/images/placeholder.png";

            Anchor detailsLink = new Anchor("product/" + p.getId(), "");
            detailsLink.getStyle().set("text-decoration", "none");

            Image img = new Image(src, p.getName());
            detailsLink.add(img);

            card.add(detailsLink);

            Div info = new Div();
            info.addClassName("product-info");
            H2 title = new H2(p.getName());
            title.getStyle().set("font-size","1.1rem").set("margin","0 0 .25rem 0");
            Span price = new Span("$" + p.getPrice());
            price.addClassName("price");
            Button add = new Button("Add to Cart", e -> {
                cartService.addToCart(p, 1);
                Notification.show("Added to cart");
            });
            add.addClassName("btn");

            info.add(title, price, add);
            card.add(info);

            Anchor detailLink = new Anchor("product/" + p.getId(), card);
            detailLink.getStyle().set("text-decoration", "none").set("color", "inherit");
            catalog.add(detailLink);
        }
    }

    private void configureCartGrid() {
        cartGrid.addColumn(ci -> ci.product.getName())
                .setHeader("Product").setAutoWidth(true);
        cartGrid.addColumn(ci -> ci.quantity)
                .setHeader("Qty").setAutoWidth(true);
        cartGrid.addComponentColumn(ci -> {
            Button rm = new Button("Remove", e -> {
                cart.remove(ci.product);
                updateCartGrid();
            });
            return rm;
        }).setHeader("Action").setAutoWidth(true);
        cartGrid.setSizeFull();
    }

    @Override
    public void buildLayout() {
        H2 pageTitle = new H2("Vegan Storefront");
        pageTitle.getStyle().set("margin","1rem");

        HorizontalLayout main = new HorizontalLayout(catalog, cartGrid);
        main.setSizeFull();
        main.setFlexGrow(2, catalog);
        main.setFlexGrow(1, cartGrid);
        main.setPadding(false);
        main.setSpacing(false);

        clearCart.addClickListener(e -> {
            cart.clear();
            updateCartGrid();
            new Notification("Cart cleared", 2000, Notification.Position.MIDDLE).open();
        });
        checkout.addClickListener(e -> doCheckout());

        HorizontalLayout actions = new HorizontalLayout(clearCart, checkout);
        actions.getStyle().set("margin","1rem");

        add(pageTitle, main, actions);
        expand(main);
    }

    private void updateCartGrid() {
        cartGrid.setItems(
            cart.entrySet().stream()
                .map(e -> new CartItem(e.getKey(), e.getValue()))
                .toList()
        );
    }

    private void doCheckout() {
        if (cart.isEmpty()) {
            new Notification("Your cart is empty", 2000, Notification.Position.MIDDLE)
                .open();
            return;
        }

        String email = SecurityContextHolder.getContext()
                            .getAuthentication().getName();
        var customer = customerRepo.findByEmail(email)
                            .orElseThrow(() -> new IllegalStateException("User not found: "+email));

        var lines = cart.entrySet().stream()
                         .map(e -> new OrderLineRequest(e.getKey().getId(), e.getValue()))
                         .toList();
        var req = new PlaceOrderRequest(customer.getId(), lines);

        try {
            OrderResponse resp = orderService.placeOrder(req);
            new Notification("Order placed (#" + resp.orderId() + ")",
                             3000, Notification.Position.MIDDLE)
                .open();
            cart.clear();
            buildProductCatalog();   // refresh stock & images
            updateCartGrid();
        } catch (Exception ex) {
            new Notification("Checkout failed: " + ex.getMessage(),
                             3000, Notification.Position.MIDDLE)
                .open();
        }
    }

    private static class CartItem {
        final Product product;
        final int quantity;
        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }
}
