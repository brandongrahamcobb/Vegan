//

//  Untitled.swift
//  
//
//  Created by Brandon Cobb on 5/18/25.
//
package com.brandongcobb.vegan.store.ui.components;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

public class ProductGallery extends Composite<VerticalLayout> {

    private final Image mainImage = new Image();

    private final HorizontalLayout thumbnails = new HorizontalLayout();

    public ProductGallery(List<String> imageUrls) {
        getContent().setSpacing(true);
        getContent().setPadding(true);

        mainImage.setWidth("400px");
        mainImage.getStyle().set("border-radius", "10px");
        getContent().add(mainImage);

        thumbnails.setSpacing(true);
        getContent().add(thumbnails);

        if (imageUrls == null || imageUrls.isEmpty()) {
            // Show a placeholder if no images
            mainImage.setSrc("frontend/images/placeholder.png");
            return;
        }

        // Initialize main image as first in list
        mainImage.setSrc(imageUrls.get(0));

        // Add thumbnails
        for (String url : imageUrls) {
            Image thumb = createThumbnail(url);
            thumbnails.add(thumb);
        }
    }

    private Image createThumbnail(String url) {
        Image thumbnail = new Image(url, "Thumbnail");
        thumbnail.setWidth("80px");
        thumbnail.getStyle().set("cursor", "pointer");
        thumbnail.getStyle().set("border-radius", "5px");
        thumbnail.getStyle().set("border", "2px solid transparent");

        // On click, update main image
        thumbnail.addClickListener(event -> {
            mainImage.setSrc(url);
            // Optionally add styling to show active thumbnail
            resetThumbnailBorders();
            thumbnail.getStyle().set("border", "2px solid #007bff");
        });

        return thumbnail;
    }

    private void resetThumbnailBorders() {
        thumbnails.getChildren().forEach(c -> {
            if (c instanceof Image) {
                c.getElement().getStyle().set("border", "2px solid transparent");
            }
        });
    }
}
