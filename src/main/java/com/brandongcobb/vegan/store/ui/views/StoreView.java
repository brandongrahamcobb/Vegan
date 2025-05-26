package com.brandongcobb.vegan.store.ui.views;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.layouts.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.brandongcobb.vegan.store.domain.ProductImage;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.vaadin.flow.component.notification.Notification;

@Route(value = "store", layout = MainLayout.class)
@PageTitle("Store | The Vyrtuous Project")
@PermitAll
public class StoreView extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService storeService;
    private final CartService cartService;
    private final OrderService orderService;
    private final VeganRepository veganRepo;

    private final VerticalLayout catalog;
    private final Div cartDropdown;
    private final Button cartBtn;
    private final Map<Product, Integer> cartSnapshot;

    private Grid<Product> productGrid;
    private TextField searchField;
    private MultiSelectComboBox<Category> categoryFilter;
    private Select<String> sortBy;
    private Checkbox inStockOnly;

    @Autowired
    public StoreView(StoreService storeService, CartService cartService,
                     OrderService orderService, VeganRepository veganRepo) {
        this.storeService = storeService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.veganRepo = veganRepo;
        this.cartSnapshot = new LinkedHashMap<>();

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // --- Cart dropdown setup ---
        cartDropdown = new Div();
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
                .set("display", "none");

        cartBtn = new Button(VaadinIcon.CART.create());
        cartBtn.addClickListener(e -> UI.getCurrent().navigate("checkout"));

        Div cartContainer = new Div(cartBtn, cartDropdown);
        cartContainer.getStyle()
                .set("position", "fixed")
                .set("bottom", "16px")
                .set("right", "16px")
                .set("z-index", "10000");

        cartContainer.getElement().addEventListener("mouseenter", e -> {
            cartDropdown.getStyle().set("display", "block");
        });
        cartContainer.getElement().addEventListener("mouseleave", e -> {
            cartDropdown.getStyle().set("display", "none");
        });

        // --- Catalog ---
        catalog = new VerticalLayout();
        catalog.addClassName("product-catalog");
        catalog.setSizeFull();

        // --- Add components to view ---
        add(cartContainer, catalog);

        // Header
        H1 header = new H1("Vegan Store");
        catalog.add(header);

        // Filters section
        HorizontalLayout filters = new HorizontalLayout();
        filters.setSpacing(true);
        filters.setAlignItems(FlexComponent.Alignment.BASELINE);

        // Search
        searchField = new TextField();
        searchField.setPlaceholder("Search products...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(e -> refreshGrid());

        // Categories
        categoryFilter = new MultiSelectComboBox<>("Categories");
        categoryFilter.setItems(storeService.listCategories());
        categoryFilter.addValueChangeListener(e -> refreshGrid());

        // Sort by
        sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Name", "Price (Low to High)", "Price (High to Low)", "Newest First");
        sortBy.setValue("Name");
        sortBy.addValueChangeListener(e -> refreshGrid());

        // In stock only
        inStockOnly = new Checkbox("Show only in stock");
        inStockOnly.addValueChangeListener(e -> refreshGrid());

        filters.add(searchField, categoryFilter, sortBy, inStockOnly);
        catalog.add(filters);

        // Product grid
        productGrid = new Grid<>(Product.class);
        productGrid.setSizeFull();
        productGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        productGrid.addSelectionListener(e -> updateCart());

        productGrid.addColumn(new ComponentRenderer<>(this::createProductCard)).setHeader("Products");
        catalog.add(productGrid);

        loadProducts();
        refreshCatalog(null);
        refreshCartGrid();
    }

    private Div createProductCard(Product product) {
        Div card = new Div();
        card.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "1em")
                .set("padding", "1em")
                .set("border", "1px solid var(--lumo-border-color)")
                .set("border-radius", "var(--lumo-border-radius)")
                .set("background", "var(--lumo-base-color)");

        ProductImage thumbnailImg = product.getThumbnail();
        Image thumbnail = new Image(thumbnailImg != null ? thumbnailImg.getUrl() : "", product.getName());
        thumbnail.setWidth("100%");
        thumbnail.setHeight("150px");
        thumbnail.addClickListener(e -> UI.getCurrent().navigate("product/" + product.getId()));
        Span brand = new Span(product.getBrand());

        Span price = new Span("$" + product.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        price.getStyle().set("font-weight", "bold");
        Span name = new Span(product.getName());
        Span stock = new Span(product.getStock() > 0 ? "In Stock" : "Out of Stock");
        stock.getStyle().set("color", product.getStock() > 0 ? "green" : "red");

        Button addToCart = new Button("Add to Cart", e -> {
            cartService.addToCart(product, 1);
            refreshCartGrid();
        });

        card.add(thumbnail, name, brand, price, stock, addToCart);
        return card;
    }

    private void loadProducts() {
        productGrid.setItems(storeService.findAllProducts());
        refreshGrid();
    }

    private void refreshGrid() {
        List<Product> filtered = storeService.findAllProducts().stream()
                .filter(this::matchesSearch)
                .filter(this::matchesCategories)
                .filter(this::matchesStock)
                .sorted((p1, p2) -> {
                    return switch (sortBy.getValue()) {
                        case "Price (Low to High)" -> p1.getPrice().compareTo(p2.getPrice());
                        case "Price (High to Low)" -> p2.getPrice().compareTo(p1.getPrice());
                        case "Newest First" -> p2.getId().compareTo(p1.getId());
                        default -> p1.getName().compareTo(p2.getName());
                    };
                }).toList();

        productGrid.setItems(filtered);
    }

    private boolean matchesSearch(Product p) {
        String term = searchField.getValue().toLowerCase();
        return term.isEmpty() || p.getName().toLowerCase().contains(term) || p.getBrand().toLowerCase().contains(term);
    }

    private boolean matchesCategories(Product p) {
        return categoryFilter.isEmpty() || categoryFilter.getValue().contains(p.getCategory());
    }

    private boolean matchesStock(Product p) {
        return !inStockOnly.getValue() || p.getStock() > 0;
    }

    private void refreshCatalog(String filter) {
        catalog.removeAll();
        String ctx = UI.getCurrent().getInternals().getContextRootRelativePath();

        List<Product> products = storeService.findAllProducts().stream()
                .filter(p -> filter == null || filter.isBlank() || p.getName().toLowerCase().contains(filter.toLowerCase()))
                .toList();

        for (Product p : products) {
            VerticalLayout card = new VerticalLayout();
            String mainUrl = p.getImages() != null && !p.getImages().isEmpty()
                ? safeImageUrl(p.getImages().get(0).getUrl())
                : "frontend/images/placeholder.png";

            Image img = new Image(mainUrl, p.getName());
            img.setWidth("100%");
            img.addClickListener(e -> UI.getCurrent().navigate("product/" + p.getId()));
            Button add = new Button("Add", e -> {
                int current = cartService.getCartItems().getOrDefault(p, 0);
                if (current >= p.getStock()) {
                    Notification.show("Not enough stock available", 3000, Notification.Position.MIDDLE);
                    return;
                }
                cartService.addToCart(p, 1);
                refreshCartGrid();
            });
            card.add(img, new Span(p.getName()), new Span("$" + p.getPrice()), add);
            card.setWidth("200px");
            catalog.add(card);
        }
    }
    
    private String safeImageUrl(String url) {
        return (url != null && !url.isBlank()) ? url : "frontend/images/placeholder.png";
    }

    private void refreshCartGrid() {
        cartDropdown.removeAll();
        Map<Product, Integer> items = cartService.getCartItems();

        if (items.isEmpty()) {
            cartDropdown.add(new Span("Cart is empty"));
        } else {
            Grid<CartItem> dropdownCart = new Grid<>(CartItem.class, false);
            dropdownCart.setAllRowsVisible(true);

            dropdownCart.addColumn(ci -> ci.product.getName()).setHeader("Product");
            dropdownCart.addColumn(ci -> ci.quantity).setHeader("Qty");
            dropdownCart.addComponentColumn(ci -> new Button("×", e -> {
                cartService.removeFromCart(ci.product);
                refreshCartGrid();
            })).setHeader("Remove");

            dropdownCart.setItems(
                    items.entrySet().stream().map(e -> new CartItem(e.getKey(), e.getValue())).toList()
            );

            cartDropdown.add(dropdownCart);
            cartDropdown.add(new Button("Go to Checkout", e -> UI.getCurrent().navigate("checkout")));
        }
    }

    private void updateCart() {
        refreshCartGrid();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            veganRepo.findByEmail(auth.getName()).ifPresent(v -> {
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
            });
        }

        refreshCatalog(null);
        refreshCartGrid();
    }

    private record CartItem(Product product, int quantity) {}
}