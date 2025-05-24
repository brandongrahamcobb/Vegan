package com.brandongcobb.vegan.store.ui.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AvatarItem extends Composite<HorizontalLayout> implements HasSize {

    private final Avatar avatar = new Avatar("?");
    private final Span heading = new Span();
    private final Span description = new Span();

    public AvatarItem() {
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        avatar.setHeight("40px");
        avatar.setWidth("40px");

        description.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "var(--lumo-font-size-s)");

        VerticalLayout column = new VerticalLayout(heading, description);
        column.setPadding(false);
        column.setSpacing(false);

        getContent().add(avatar, column);
        getContent().getStyle().set("line-height", "var(--lumo-line-height-m)");
    }

    public AvatarItem(String heading, String description, Avatar customAvatar) {
        this();
        setHeading(heading);
        setDescription(description);
        setAvatar(customAvatar);
    }

    public void setHeading(String text) {
        heading.setText(text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setAvatar(Avatar newAvatar) {
        avatar.setName(newAvatar.getName());
        avatar.setImage(newAvatar.getImage());
    }

    public Avatar getAvatar() {
        return avatar;
    }
}
