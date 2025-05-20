package com.brandongcobb.vegan.store.ui.views;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.brandongcobb.vegan.store.ui.layouts.*;
import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.brandongcobb.vegan.store.ui.base.*;
import org.springframework.stereotype.Component;

@Component
@Route(value = "admin/categories", layout = AdminLayout.class)
@PageTitle("Categories")
public class AdminCategoryView extends View {

    private final StoreService service;

    // UI components
    private final Grid<Category> grid = new Grid<>(Category.class, false);
    private final TextField nameField = new TextField("Name");
    private final ComboBox<Category> parentCombo = new ComboBox<>("Parent Category");
    private final Button save = new Button("Save"),
                         delete = new Button("Delete"),
                         clear = new Button("Clear");
    private final Binder<Category> binder = new Binder<>(Category.class);
    private Category current;

    @Autowired
    public AdminCategoryView(StoreService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();
    }

    private void configureGrid() {
        grid.addColumn(Category::getId).setHeader("ID")
          .setAutoWidth(true);
        grid.addColumn(Category::getFullPath)
          .setHeader("Category Path")
          .setAutoWidth(true)
          .setSortable(true);
        grid.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));
        grid.setSizeFull();
    }

    private void configureForm() {
      // name binding
        binder.forField(nameField)
            .asRequired("Name is required")
            .withValidator(new StringLengthValidator("1–50 chars",1,50))
            .bind(Category::getName, Category::setName);

      // parent binding
        parentCombo.setItemLabelGenerator(Category::getFullPath);
        binder.forField(parentCombo)
          .bind(Category::getParent, Category::setParent);

        save.addClickListener(e -> saveCategory());
        delete.addClickListener(e -> confirmDelete());
        clear.addClickListener(e -> clearForm());
    }

    @Override
    public void buildLayout() {
        H2 title = new H2("Manage Categories");
        FormLayout form = new FormLayout(nameField, parentCombo);
        HorizontalLayout buttons = new HorizontalLayout(save, delete, clear);
        VerticalLayout formContainer = new VerticalLayout(title, form, buttons);
        formContainer.setWidth("350px");

        HorizontalLayout main = new HorizontalLayout(grid, formContainer);
        main.setSizeFull();
        main.setFlexGrow(2, grid);
        main.setFlexGrow(1, formContainer);

        add(main);
    }

    private void edit(Category c) {
        if (c == null) {
            clearForm();
        } else {
            current = service.findCategoryById(c.getId()).orElse(c);
            binder.readBean(current);
            delete.setEnabled(true);
            save.setText("Update");
        }
    }

    private void saveCategory() {
        try {
              if (current == null) current = new Category();
              binder.writeBean(current);
              service.saveCategory(current);
              Notification.show("Saved",2000,Position.MIDDLE);
              updateGrid();
              clearForm();
        } catch (ValidationException e) {
              Notification.show("Fix errors",2000,Position.MIDDLE);
        }
    }

    private void confirmDelete() {
        if (current==null || current.getId()==null) {
            Notification.show("Select one",2000,Position.MIDDLE);
            return;
        }
        ConfirmDialog dialog = new ConfirmDialog(
            "Delete?",
            "Really delete “"+current.getFullPath()+"”?",
            "Delete", ev -> {
                service.deleteCategoryById(current.getId());
                Notification.show("Deleted",2000,Position.MIDDLE);
                updateGrid(); clearForm();
            },
            "Cancel", ev -> {}
        );
        dialog.open();
    }

    private void clearForm() {
        current = null;
        binder.readBean(new Category());
        grid.deselectAll();
        delete.setEnabled(false);
        save.setText("Save");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        updateGrid();
        clearForm();
    }

    private void updateGrid() {
        var items = service.listAllCategories();
        grid.setItems(items);
        parentCombo.setItems(items);
    }
}
