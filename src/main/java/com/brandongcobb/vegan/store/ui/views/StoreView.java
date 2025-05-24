package com.brandongcobb.vegan.store.ui.views;

import com.vaadin.flow.server.VaadinSession;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.service.OrderService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import com.brandongcobb.vegan.store.service.CartService;

import com.vaadin.flow.router.BeforeEnterObserver;

import com.vaadin.flow.router.BeforeEnterEvent;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import com.brandongcobb.components.avataritem.AvatarItem;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;

@Route("store")
@PageTitle("Store | The Vyrtuous Project")
public class StoreView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    
    private final StoreService      storeService;
    private final CartService       cartService;
    private final OrderService      orderService;
    private final VeganRepository veganRepo;
    
    // local snapshot
    private final Map<Product,Integer> cartSnapshot = new LinkedHashMap<>();
    
    // === header widgets ===
    private final H1 title          = new H1("The Vyrtuous Project");
    private final TextField search= new TextField();
    private final MenuBar menu       = new MenuBar();
    private final Button cartBtn     = new Button(com.vaadin.flow.component.icon.VaadinIcon.CART.create());
    private final Div    cartDropdown= new Div();
    private final AvatarItem avatarItem = new AvatarItem();
    private final Div    accountDropdown = new Div();
    
    // === main content ===
    private final VerticalLayout catalog   = new VerticalLayout();
    private final Grid<CartItem> cartGrid  = new Grid<>(CartItem.class, false);
    private final Button checkout  = new Button("Go to Checkout");
    private final Button account   = new Button("My Account");
    
    @Autowired
    public StoreView(StoreService storeService,
                     CartService cartService,
                     OrderService orderService,
                     VeganRepository veganRepo) {
        this.storeService   = storeService;
        this.cartService    = cartService;
        this.orderService   = orderService;
        this.veganRepo   = veganRepo;
        
        // --- build header ---
        title.addClassName(Gap.MEDIUM);
        
        // search box
        search.setPlaceholder("Search the project…");
        search.setClearButtonVisible(true);
        search.getStyle().set("width", "800px");
        search.addValueChangeListener(e -> refreshCatalog(e.getValue()));
        
        // menu
        //MenuItem home = menu.addItem("Home", e -> UI.getCurrent().navigate(""));
        //MenuItem store = menu.addItem("Store", e -> UI.getCurrent().navigate("store"));
        //MenuItem about = menu.addItem("About", e -> UI.getCurrent().navigate("about"));
        
        // cart button + dropdown
        
        cartDropdown.addClassName("cart-dropdown");
        cartDropdown.getStyle()
            .set("position", "absolute")
            .set("top", "100%") // Position below the button
            .set("left", "0")
            .set("background", "white")
            .set("border", "1px solid #bbb")
            .set("padding", "1em")
            .set("min-width", "200px") // Ensure it’s clickable area
            .set("z-index", "1000")
            .set("display", "none");

        // Wrap both in a relative container
        Div cartContainer = new Div(cartBtn, cartDropdown);
        cartContainer.getStyle().set("position", "relative");

        // Show on hover over the container (button or dropdown)
        cartContainer.getElement().addEventListener("mouseenter", e -> {
            cartDropdown.getStyle().set("display", "block");
        });
        cartContainer.getElement().addEventListener("mouseleave", e -> {
            cartDropdown.getStyle().set("display", "none");
        });

        cartBtn.addClickListener(e -> UI.getCurrent().navigate("checkout"));
        
        // account avatar + dropdown
        accountDropdown.addClassName("account-dropdown");
        accountDropdown.getStyle().set("position","absolute")
        .set("background","white")
        .set("border","1px solid #bbb")
        .set("padding","1em")
        .set("display","none")
        .set("z-index", "1000");
        Div avatarContainer = new Div(avatarItem, accountDropdown);
        avatarContainer.getElement().addEventListener("mouseenter", ev -> accountDropdown.getStyle().set("display","block"));
        avatarContainer.getElement().addEventListener("mouseleave", ev -> accountDropdown.getStyle().set("display","none"));
        avatarItem.getContent().addClickListener(e -> UI.getCurrent().navigate("account"));
        avatarContainer.getStyle().set("position", "relative");

        // put header pieces into a row
        HorizontalLayout header = new HorizontalLayout(title, search, menu, cartContainer, avatarContainer);
        header.setWidthFull();
        header.expand(title, menu);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().add(header);
        
        // --- build main area: catalog | cart grid + nav button ---
        catalog.addClassName("product-catalog");
        catalog.setSizeFull();
        configureCartGrid();
        
        checkout.addClickListener(e -> UI.getCurrent().navigate("checkout"));
        avatarContainer.addClickListener(e -> UI.getCurrent().navigate("account"));
        HorizontalLayout actions = new HorizontalLayout(account, checkout);
        
        SplitLayout split = new SplitLayout(catalog, new VerticalLayout());
        split.setSizeFull();
        split.setSplitterPosition(70);
        getContent().add(split);
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
                messages.add(new Span("👽" + input));
                userInput.clear();
                Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
            }
        });

        botPopup.add(messages, userInput, send);
        botIcon.addClickListener(e -> {
            boolean visible = "block".equals(botPopup.getStyle().get("display"));
            botPopup.getStyle().set("display", visible ? "none" : "block");
        });
        getContent().add(botIcon, botPopup);

//        .addClickListener(e -> {
//            botPopup.getStyle().set("display", botPopup.getStyle().get("display").equals("none") ? "block" : "none");
//        });
        getContent().setSizeFull();
    }

    private void refreshCatalog(String filter) {
        catalog.removeAll();  // if `catalog` is a Composite, otherwise just catalog.removeAll()
        String ctx = com.vaadin.flow.server.VaadinServletRequest.getCurrent().getContextPath();
        List<Product> prods = storeService.findAllProducts().stream()
            .filter(p -> filter == null || filter.isBlank()
                || p.getName().toLowerCase().contains(filter.toLowerCase()))
            .toList();

        prods.forEach(p -> {
            VerticalLayout card = new VerticalLayout();
            card.addClassName("product-card");

            String img = (p.getImageUrl() == null) ? ctx + "/images/placeholder.png" : ctx + p.getImageUrl();
            Image image = new Image(img, p.getName());
            image.setWidth("100%");
            image.addClickListener(ev ->
                UI.getCurrent().navigate("product/" + p.getId())
            );

            Span name = new Span(p.getName());
            Span price = new Span("$" + p.getPrice());

            Button addToCart = new Button("Add", ev -> {
                cartService.addToCart(p, 1);
                refreshCartGrid();
            });

            card.add(image, name, price, addToCart);
            card.setWidth("200px");

            catalog.add(card);  // If catalog is a Composite<VerticalLayout>
        });
    }
    
    private void configureCartGrid() {
        cartGrid.removeAllColumns();
        cartGrid.addColumn(ci->ci.product.getName()).setHeader("Product");
        cartGrid.addColumn(ci->ci.quantity).setHeader("Qty");
        cartGrid.addComponentColumn(ci->{
            Button rm = new Button("×", e->{
                cartService.removeFromCart(ci.product);
                refreshCartGrid();
            });
            return rm;
        }).setHeader("Remove");
        cartGrid.setSizeFull();
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
        // sync avatar with logged‐in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null && auth.isAuthenticated()) {
            String email = auth.getName();
            veganRepo.findByEmail(email).ifPresent(v -> {
                String full = v.getFirstName()+" "+v.getLastName();
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
                Avatar avatar = new Avatar(full);
                avatarItem.setAvatar(avatar);
                avatarItem.setHeading(full);
                avatarItem.setDescription(v.getEmail());
            });
        }
        // populate content
        refreshCatalog(null);
        refreshCartGrid();

        accountDropdown.removeAll();
        accountDropdown.add(
                            new Button("My Account",  e -> UI.getCurrent().navigate("account")),
                            new Button("My Orders",   e -> UI.getCurrent().navigate("orders")),
                            new Button("Logout",      e -> UI.getCurrent().getPage()
                                       .executeJs("fetch('/logout',{method:'POST'}).then(_=>location.href='/login')"))
                            );
    }

    private record CartItem(Product product, int quantity) {}
}
