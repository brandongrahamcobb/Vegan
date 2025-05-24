package com.brandongcobb.vegan.store.ui.layouts;

import com.brandongcobb.vegan.store.domain.Vegan;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.ui.components.AvatarItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public class MainLayout extends AppLayout {

    private final VeganRepository veganRepo;
    private final Button myAccountButton = new Button("My Account");
    private final Div avatarClickable = new Div();
    private final H1 title = new H1("Love is Vegan");
    private final TextField search = new TextField();
    private final AvatarItem avatarItem = new AvatarItem();
    private final Avatar avatar = new Avatar();
    private final VerticalLayout accountDropdown = new VerticalLayout();

    @Autowired
    public MainLayout(VeganRepository veganRepo) {
        this.veganRepo = veganRepo;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        avatarClickable.add(avatarItem);
        avatarClickable.getStyle().set("cursor", "pointer");
        avatarClickable.addClickListener(e -> toggleDropdown());

        accountDropdown.setVisible(false); // initial hidden state
        accountDropdown.setSpacing(false);
        accountDropdown.setPadding(false);
        accountDropdown.getStyle()
                .set("background", "white")
                .set("border", "1px solid #ccc")
                .set("box-shadow", "0 2px 6px rgba(0, 0, 0, 0.15)")
                .set("z-index", "1000")
                .set("position", "absolute")
                .set("top", "100%")
                .set("right", "0")
                .set("min-width", "160px")
                .set("padding", "0.5rem")
                .set("border-radius", "var(--lumo-border-radius)");

        Div avatarContainer = new Div(avatarClickable, accountDropdown);
        avatarContainer.getStyle().set("position", "relative");
        avatarContainer.setWidth("auto");

        // Optional: Close dropdown on mouse leave
        avatarContainer.getElement().addEventListener("mouseleave", e -> {
            accountDropdown.setVisible(false);
        });

        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        Image logo = new Image("images/veganislove2.png", "Vegan Store Logo");
        logo.setHeight("40px");
        logo.getStyle().set("margin", "var(--lumo-space-m)");

        search.setPlaceholder("Search the project…");
        search.setClearButtonVisible(true);
        search.getStyle().set("width", "1000px");

        HorizontalLayout headerRow = new HorizontalLayout(toggle, logo, search, avatarContainer);
        headerRow.setWidthFull();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);
        headerRow.expand(title);
        headerRow.setSpacing(true);

        addToNavbar(headerRow);
    }

    private void addDrawerContent() {
        Span appName = new Span("");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        // Example navigation items - replace with your actual menu entries
        nav.addItem(new SideNavItem("Store", "store", new SvgIcon("images/store.svg")));
        nav.addItem(new SideNavItem("Account", "account"));
        nav.addItem(new SideNavItem("Orders", "orders"));

        return nav;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.setText("© 2025 Vegan Store");
        return footer;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Hide avatar on certain routes
        String route = UI.getCurrent().getInternals().getActiveViewLocation().getPath();
        boolean hideHeader = route.equals("login") || route.equals("register") || route.startsWith("admin");

        avatarItem.setVisible(!hideHeader);
        accountDropdown.setVisible(false);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !hideHeader) {
            String email = auth.getName();
            Optional<Vegan> userOpt = veganRepo.findByEmail(email);
            if (userOpt.isPresent()) {
                Vegan user = userOpt.get();
                String fullName = user.getFirstName() + " " + user.getLastName();

                VaadinSession.getCurrent().setAttribute("userId", user.getId());

                avatarItem.setAvatar(new Avatar(fullName));  // reuse avatarItem's method
                avatarItem.setHeading(fullName);
                avatarItem.setDescription(user.getEmail());

                if (accountDropdown.getComponentCount() == 0) {
                    myAccountButton.addClickListener(e -> UI.getCurrent().navigate("account"));
                    
                    accountDropdown.add(
                        myAccountButton,
                        new Button("My Orders", e -> UI.getCurrent().navigate("orders")),
                        new Button("Logout", e -> UI.getCurrent().getPage()
                                .executeJs("fetch('/logout',{method:'POST'}).then(_ => location.href='/login')"))
                    );
                }
            }
        }
    }

    private void toggleDropdown() {
        accountDropdown.setVisible(!accountDropdown.isVisible());
    }
}
