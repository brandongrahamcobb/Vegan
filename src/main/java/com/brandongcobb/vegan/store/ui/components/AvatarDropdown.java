package com.brandongcobb.vegan.store.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AvatarDropdown extends Div {

    public AvatarDropdown() {
        // Avatar + Name (account trigger)
        Avatar avatar = new Avatar("User");
        SpanWithName accountItem = new SpanWithName("Account", avatar);

        // Dropdown container
        VerticalLayout dropdown = new VerticalLayout();
        dropdown.addClassName("account-dropdown");

        dropdown.getStyle()
            .set("position", "absolute")
            .set("background", "white")
            .set("border", "1px solid #ccc")
            .set("padding", "0.5rem")
            .set("display", "none")
            .set("z-index", "1000")
            .set("box-shadow", "0px 2px 6px rgba(0,0,0,0.1)")
            .set("font-size", "0.8rem")
            .set("min-width", "100%");

        // Links (each is a row, no button style)
        dropdown.add(createLink("My Profile", "/account"));
        dropdown.add(createLink("Settings", "/settings"));
        dropdown.add(createLink("Logout", "/logout"));

        // Container (hover reveal)
        Div container = new Div(accountItem, dropdown);
        container.getStyle().set("position", "relative");
        container.addClassName("avatar-dropdown-container");

        // Hover behavior
        container.getElement().addEventListener("mouseenter", ev -> dropdown.getStyle().set("display", "block"));
        container.getElement().addEventListener("mouseleave", ev -> dropdown.getStyle().set("display", "none"));

        // Main avatar click behavior
        accountItem.addClickListener((ClickEvent<Div> e) -> UI.getCurrent().navigate("account"));

        add(container);
    }

    private Anchor createLink(String text, String href) {
        Anchor link = new Anchor(href, text);
        link.getStyle()
            .set("display", "block")
            .set("padding", "0.25rem 0")
            .set("text-decoration", "none")
            .set("color", "black");
        return link;
    }

    // Simple span that wraps name + avatar (acts like a clickable label)
    private static class SpanWithName extends Div {
        public SpanWithName(String name, Avatar avatar) {
            setClassName("account-label");
            getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem")
                .set("cursor", "pointer");

            add(avatar, new com.vaadin.flow.component.html.Span(name));
        }
    }
}
