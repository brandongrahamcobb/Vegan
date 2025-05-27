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

import com.brandongcobb.vegan.store.ui.components.CartButtonAndDropdown; // New import
import com.brandongcobb.vegan.store.ui.components.ProductCard; // New import

@Route(value = "store", layout = MainLayout.class)
@PageTitle("Store | The Vyrtuous Project")
@PermitAll
public class StoreView extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService storeService;
    private final CartService cartService;
    private final OrderService orderService;
    private final VeganRepository veganRepo;

    private final VerticalLayout catalog;
    private final CartButtonAndDropdown cartButtonAndDropdown; // New component instance

    private Grid<Product> productGrid;
    private TextField searchField;
    private MultiSelectComboBox<Category> categoryFilter;
    private Select<String> sortBy;
    private Checkbox inStockOnly;

    @Autowired
    public StoreView(StoreService storeService, CartService cartService,
                     OrderService orderService, VeganRepository veganRepo,
                     CartButtonAndDropdown cartButtonAndDropdown) { // Inject new component
        this.storeService = storeService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.veganRepo = veganRepo;
        this.cartButtonAndDropdown = cartButtonAndDropdown; // Assign injected component

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // --- Add new CartButtonAndDropdown component ---
        add(cartButtonAndDropdown);

        // --- Catalog ---
        catalog = new VerticalLayout();
        catalog.addClassName("product-catalog");
        catalog.setSizeFull();

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
        // Removed productGrid.addSelectionListener(e -> updateCart()); as it's no longer needed here

        // Use the new ProductCard component for rendering products
        productGrid.addColumn(new ComponentRenderer<>(product ->
                new ProductCard(product, cartService, p -> cartButtonAndDropdown.refresh()) // Pass callback to refresh cart dropdown
        )).setHeader("Products");
        catalog.add(productGrid);

        add(catalog); // Add catalog to the main view

        loadProducts();
        // refreshCatalog(null); // Removed, as grid handles display
        // cartButtonAndDropdown.refresh(); // Removed from constructor, will be called by onAttach in CartButtonAndDropdown
    }

    // Removed createProductCard method as it's now in ProductCard component

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

    // Removed refreshCatalog method

    // Removed safeImageUrl method as it's now handled within ProductCard

    // Removed refreshCartGrid method as it's now handled within CartButtonAndDropdown

    // Removed updateCart method as it's now handled by the callback to CartButtonAndDropdown

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            veganRepo.findByEmail(auth.getName()).ifPresent(v -> {
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
            });
        }

        // refreshCatalog(null); // Removed
        cartButtonAndDropdown.refresh(); // Ensure cart dropdown is up-to-date
    }

    // Removed CartItem record as it's now in CartButtonAndDropdown (as CartItemDisplay)
}
