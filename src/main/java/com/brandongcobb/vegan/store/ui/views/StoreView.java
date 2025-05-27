package com.brandongcobb.vegan.store.ui.views;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.layouts.MainLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import com.brandongcobb.vegan.store.ui.components.CartButtonAndDropdown;
import com.brandongcobb.vegan.store.ui.components.CategoryCarousel;
import com.brandongcobb.vegan.store.service.FileStorageService;

@Route(value = "store", layout = MainLayout.class)
@PageTitle("Store | The Vyrtuous Project")
@PermitAll
public class StoreView extends VerticalLayout implements BeforeEnterObserver {

    private final StoreService storeService;
    private final CartService cartService;
    private final VeganRepository veganRepo;
    private final CartButtonAndDropdown cartButtonAndDropdown;
    private final FileStorageService storageService;

    @Autowired
    public StoreView(StoreService storeService, CartService cartService,
                     VeganRepository veganRepo, CartButtonAndDropdown cartButtonAndDropdown, FileStorageService storageService) {
        this.storeService = storeService;
        this.cartService = cartService;
        this.veganRepo = veganRepo;
        this.cartButtonAndDropdown = cartButtonAndDropdown;
        this.storageService = storageService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        add(cartButtonAndDropdown);

        H1 header = new H1("Vegan Store");
        add(header);

        // Fetch categories and products
        List<Category> categories = storeService.getCategoryList();
        List<Product> products = storeService.getProductList();

        // Create and add carousels for each category
        categories.forEach(category -> {
            List<Product> categoryProducts = products.stream()
                    .filter(product -> product.getCategory() != null && product.getCategory().equals(category))
                    .toList();
            if (!categoryProducts.isEmpty()) {
                CategoryCarousel carousel = new CategoryCarousel(category, categoryProducts, cartService, cartButtonAndDropdown::refresh);
                add(carousel);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            veganRepo.findByEmail(auth.getName()).ifPresent(v -> {
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
            });
        }
        cartButtonAndDropdown.refresh();
    }
}