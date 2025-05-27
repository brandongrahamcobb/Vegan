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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import com.brandongcobb.vegan.store.domain.Category;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.brandongcobb.vegan.store.ui.base.*;
import org.springframework.stereotype.Component;
import com.vaadin.flow.router.BeforeEnterEvent;

@Route(value = "store", layout = MainLayout.class)
@PageTitle("Store | The Vyrtuous Project")
public class StoreView extends View {

    private final StoreService service;
    private final FileStorageService storageService;
    private final Grid<Product> grid = new Grid<>(Product.class, false);
    private final TextField nameField = new TextField("Name");
    private final TextArea descriptionField = new TextArea("Description");
    private final NumberField priceField = new NumberField("Price");
    private final IntegerField stockField = new IntegerField("Stock");
    private final ComboBox<Category> categoryCombo = new ComboBox<>("Category");
    private final Button save   = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button clear  = new Button("Clear");
    private final Binder<Product> binder = new Binder<>(Product.class);
    private Product currentProduct;
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Image preview = new Image();
    private final TextField brandField = new TextField("Brand");
    private final TextField dimensionsField = new TextField("Dimensions (L×W×H)");
    private final NumberField weightField = new NumberField("Weight (kg)");
    private final TextField tagsField = new TextField("Tags (comma-separated)");
    private final TextField seoTitleField = new TextField("SEO Title");
    private final TextField seoKeywordsField = new TextField("SEO Keywords");
    private final TextArea seoDescriptionField = new TextArea("SEO Description");
    private final Checkbox activeField = new Checkbox("Active");
    private final DatePicker addedDateField = new DatePicker("Date Added");
    private final Div galleryContainer = new Div();

    @Autowired
    public StoreView(StoreService service,
                       FileStorageService storageService) {
        this.service = service;
        this.storageService = storageService;
        // ... (rest of the constructor)
    }

    // ... (rest of the class)
}
