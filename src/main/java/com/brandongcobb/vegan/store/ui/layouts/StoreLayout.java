package com.brandongcobb.vegan.store.ui.layouts;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.StoreService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.brandongcobb.vegan.store.service.CartService;
import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.service.CartService;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.service.CartService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class StoreLayout extends AppLayout {
    
    private final StoreService storeService;
    private final Map<Product,Integer> cart = new LinkedHashMap<>();
    private final CartService cartService;
    
    @Autowired
    public StoreLayout(StoreService storeService, CartService cartService) {
        this.storeService = storeService;
        this.cartService = cartService;
        
        // 1) Header with hamburger toggle, logo, cart button
        DrawerToggle toggle = new DrawerToggle();
        H1 logo = new H1("Vegan Store"); // or “Shop Vegan Essentials”
        Button cartButton = new Button(VaadinIcon.CART.create());
        cartButton.addClassName("cart-button");
        
        Div cartPreview = new Div();
        cartPreview.addClassName("cart-preview");
        cartPreview.setVisible(false);
        
        cartButton.getElement().addEventListener("mouseenter", e -> cartPreview.setVisible(true));
        cartButton.getElement().addEventListener("mouseleave", e -> cartPreview.setVisible(false));
        cartPreview.getElement().addEventListener("mouseleave", e -> cartPreview.setVisible(false));
        
        // Populate this dynamically from a shared cart service or session object
        cartPreview.add(new Span("Cart is empty")); // Placeholder
        
        cartButton.addClickListener(e -> UI.getCurrent().navigate("checkout"));
        
        Div cartContainer = new Div(cartButton, cartPreview);
        cartContainer.addClassName("cart-container");
        
        
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        // top‐level categories
        for (Category cat : storeService.listCategories()) {
            MenuItem catItem = menuBar.addItem(cat.getName());
            List<Category> children = storeService.findSubCategories(cat.getId());
            if (children != null && !children.isEmpty()) {
                SubMenu sub = catItem.getSubMenu();
                for (Category subCat : children) {
                    sub.addItem(subCat.getName(), e ->
                                UI.getCurrent().navigate("store?category=" + subCat.getId()));
                }
            } else {
                catItem.addClickListener(e ->
                                        UI.getCurrent().navigate("store?category=" + cat.getId()));
            }
        }


  //     HorizontalLayout header = new HorizontalLayout(toggle, logo, cartContainer);
  //     header.expand(logo);
  //     header.setWidthFull();
  //     header.setAlignItems(FlexComponent.Alignment.CENTER);
  //     addToNavbar(header);
  //     addCartHover(cartButton, cartService);
  //
  //     // 2) Drawer menu with categories
  //     VerticalLayout menu = new VerticalLayout();
  //     menu.setPadding(false);
  //     menu.setSpacing(false);
  //     for (Category c : storeService.listCategories()) {
  //         Button link = new Button(c.getName(),
  //                                  e -> UI.getCurrent().navigate("store?category=" + c.getId()));
  //         menu.add(link);
  //     }

//        TreeGrid<Category> tree = new TreeGrid<>();
//        tree.addHierarchyColumn(Category::getName)
//        .setHeader("Browse Categories");
//        // supply roots and child‐provider
//        tree.setItems(
//                      storeService.findRootCategories(),             // top‐level
//                      parent -> storeService.findSubCategories(parent.getId())
//                      );
//        tree.addSelectionListener(e -> e.getFirstSelectedItem().ifPresent(c ->
//                                                                          UI.getCurrent().navigate("store?category=" + c.getId())
//                                                                          ));
//
//        addToDrawer(tree);
        addToDrawer(menuBar);
    }
    
    private void addCartHover(Button cartButton, CartService cartService) {
        Div preview = new Div();
        preview.addClassName("cart-hover-preview");
        preview.setVisible(false);

        cartButton.getElement().addEventListener("mouseover", e -> preview.setVisible(true));
        cartButton.getElement().addEventListener("mouseout", e -> preview.setVisible(false));

        cartButton.addClickListener(e -> UI.getCurrent().navigate("checkout"));

        cartService.getCartItems().forEach((product, qty) -> {
            Span item = new Span(product.getName() + " x" + qty);
            preview.add(item);
        });

        addToNavbar(preview);
    }
}
