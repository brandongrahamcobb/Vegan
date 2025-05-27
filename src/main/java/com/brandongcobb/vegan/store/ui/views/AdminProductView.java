package com.brandongcobb.vegan.store.ui.views;
import com.brandongcobb.vegan.store.ui.views.*;
import com.brandongcobb.vegan.store.ui.layouts.*;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.service.FileStorageService;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.components.ProductGallery;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.brandongcobb.vegan.store.ui.base.*;
import org.springframework.stereotype.Component;
import com.vaadin.flow.router.BeforeEnterEvent;

@Route(value = "admin/products", layout = AdminLayout.class)
@PageTitle("Products")
public class AdminProductView extends View {

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
    public AdminProductView(StoreService service,
                       FileStorageService storageService) {
        this.service = service;
        this.storageService = storageService;
        getContent().setSizeFull();
        configureGrid();
        configureForm();
    }

    private void configureGrid() {
        grid.addColumn(Product::getId)
            .setHeader("ID").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getName)
            .setHeader("Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(p -> Optional.ofNullable(p.getCategory())
                                   .map(Category::getName).orElse(""))
            .setHeader("Category").setAutoWidth(true);
        grid.addColumn(Product::getPrice)
            .setHeader("Price").setSortable(true).setAutoWidth(true);
        grid.addColumn(Product::getStock)
            .setHeader("Stock").setSortable(true).setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));
        grid.setSizeFull();
    }

    private void configureForm() {
        categoryCombo.setItemLabelGenerator(Category::getName);

        binder.forField(nameField)
              .asRequired("Name is required")
              .withValidator(n -> n.length() <= 150, "Max 150 chars")
              .bind(Product::getName, Product::setName);

        binder.forField(descriptionField)
              .withValidator(d -> d.length() <= 1000, "Max 1000 chars")
              .bind(Product::getDescription, Product::setDescription);

        // Two-lambda converter Double <-> BigDecimal
        binder.forField(priceField)
              .withConverter(
                  dbl -> dbl != null ? BigDecimal.valueOf(dbl) : null,
                  bd  -> bd != null ? bd.doubleValue() : 0.0
              )
              .asRequired("Price required")
              .withValidator(
                  price -> price != null && price.compareTo(BigDecimal.valueOf(0.01)) >= 0,
                  "Must be ≥ 0.01"
              )
              .bind(Product::getPrice, Product::setPrice);

        binder.forField(stockField)
              .asRequired("Stock required")
              .withValidator(new IntegerRangeValidator("Must be ≥ 0", 0, null))
              .bind(Product::getStock, Product::setStock);

        binder.forField(categoryCombo)
              .asRequired("Category required")
              .bind(Product::getCategory, Product::setCategory);
        
        binder.forField(brandField).bind(Product::getBrand, Product::setBrand);
        binder.forField(dimensionsField).bind(Product::getDimensions, Product::setDimensions);
        binder.forField(weightField).bind(Product::getWeight, Product::setWeight);
        binder.forField(tagsField).bind(Product::getTags, Product::setTags);
        binder.forField(seoTitleField).bind(Product::getSeoTitle, Product::setSeoTitle);
        binder.forField(seoKeywordsField).bind(Product::getSeoKeywords, Product::setSeoKeywords);
        binder.forField(seoDescriptionField).bind(Product::getSeoDescription, Product::setSeoDescription);
        binder.forField(activeField).bind(Product::isActive, Product::setActive);
        binder.forField(addedDateField).bind(Product::getDateAdded, Product::setDateAdded);
        
        save.addClickListener(e -> saveProduct());
        delete.addClickListener(e -> confirmDelete());
        clear.addClickListener(e -> clearForm());

        upload.setAcceptedFileTypes("image/png","image/jpeg");
        upload.addSucceededListener(evt -> {
            // The logic here was slightly off, it was trying to display the gallery
            // before the image was actually added to the product and saved.
            // The addImage method saves the product, so we just need to refresh the gallery after that.
            String filename = evt.getFileName();
            try (InputStream is = buffer.getInputStream()) {
                String url = storageService.store(filename, is);
                if (currentProduct != null && currentProduct.getId() != null) {
                    service.addImage(currentProduct.getId(), url);
                    Notification.show("Image added", 2000, Notification.Position.MIDDLE);
                    // Re-fetch the product to get updated image list and then display gallery
                    currentProduct = service.transactFindProductById(currentProduct.getId()).orElse(currentProduct);
                    displayGallery();
                } else {
                    Notification.show("Please save the product first before adding images.", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("Upload failed: " + ex.getMessage());
            }
        });
    }

    private void displayGallery() {
        galleryContainer.removeAll();
        if (currentProduct != null && currentProduct.getImageUrls() != null && !currentProduct.getImageUrls().isEmpty()) {
            ProductGallery gallery = new ProductGallery(currentProduct.getImageUrls());
            galleryContainer.add(gallery);
        }
    }

    @Override
    public void buildLayout() {
        H2 header = new H2("Product Management");

        FormLayout form = new FormLayout();
        form.add(nameField, descriptionField, priceField, stockField, categoryCombo);
        form.add(
            brandField, dimensionsField, weightField,
            tagsField, seoTitleField, seoKeywordsField, seoDescriptionField,
            activeField, addedDateField
        );
        form.add(upload, preview);
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",   1),
            new FormLayout.ResponsiveStep("600px", 2),
            new FormLayout.ResponsiveStep("1000px", 3)
        );

        HorizontalLayout buttons = new HorizontalLayout(save, delete, clear);
        buttons.setPadding(true);

        VerticalLayout formContainer = new VerticalLayout(header, form, buttons);
        formContainer.setWidth("420px");
        formContainer.setPadding(true);
        
        galleryContainer.setWidth("420px");
        formContainer.add(galleryContainer);

        grid.addComponentColumn(p -> {
            String src = (p.getImages() != null && !p.getImages().isEmpty())
                ? p.getImages().get(0).getUrl()
                : "frontend/images/placeholder.png";
            Image img = new Image(src, p.getName());
            img.setHeight("50px");
            return img;
        }).setHeader("Thumb");

        HorizontalLayout main = new HorizontalLayout(grid, formContainer);
        main.setSizeFull();
        main.setFlexGrow(2, grid);
        main.setFlexGrow(1, formContainer);

        getContent().add(main);
    }

    private void edit(Product p) {
        if (p == null) {
            clearForm();
            return;
        }
        // Ensure we get the product with its images loaded
        currentProduct = service.transactFindProductById(p.getId()).orElse(p);
        binder.readBean(currentProduct);
        delete.setEnabled(true);
        save.setText("Update");
        displayGallery();
    }

    private void saveProduct() {
        try {
            if (currentProduct == null) {
                currentProduct = new Product();
            }
            binder.writeBean(currentProduct);
            Product savedProduct = service.saveProduct(currentProduct); // Get the saved product
            currentProduct = savedProduct; // Update currentProduct with the saved one (which now has an ID)
            Notification.show("Product saved", 2000, Notification.Position.MIDDLE);
            updateGrid();
            // No clearForm() here, so user can immediately add images to the newly saved product
            // clearForm(); // Removed to allow immediate image upload
            displayGallery(); // Refresh gallery in case product was new and now has an ID
        } catch (Exception ex) {
            Notification.show("Please fix validation errors: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void confirmDelete() {
        if (currentProduct == null || currentProduct.getId() == null) {
            Notification.show("No product selected", 2000, Notification.Position.MIDDLE);
            return;
        }
        ConfirmDialog dlg = new ConfirmDialog(
            "Delete Product",
            "Delete \"" + currentProduct.getName() + "\"?",
            "Delete", evt -> {
                service.deleteProductById(currentProduct.getId());
                Notification.show("Product deleted", 2000, Notification.Position.MIDDLE);
                updateGrid();
                clearForm();
            },
            "Cancel", e -> {}
        );
        dlg.open();
    }

    private void clearForm() {
        currentProduct = null;
        binder.readBean(new Product());
        grid.deselectAll();
        delete.setEnabled(false);
        save.setText("Save");
        galleryContainer.removeAll(); // Clear gallery display
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        updateGrid();
        clearForm();
    }

    private void updateGrid() {
        grid.setItems(service.getProductList());
        categoryCombo.setItems(service.getCategoryList());
    }
}
