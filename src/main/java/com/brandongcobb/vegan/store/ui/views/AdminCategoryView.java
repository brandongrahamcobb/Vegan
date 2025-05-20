package com.brandongcobb.vegan.store.ui.views;

import com.brandongcobb.vegan.store.ui.layouts.*;
import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;
import com.brandongcobb.vegan.store.ui.base.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brandongcobb.vegan.store.domain.Category;
import com.brandongcobb.vegan.store.service.StoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "admin/categories", layout = AdminLayout.class)
@PageTitle("Categories")
public class AdminCategoryView extends VerticalLayout implements BeforeEnterObserver {
    
    private final StoreService service;
    
    // UI components
    private final TreeGrid<Category> tree       = new TreeGrid<>();
    private final TextField        filter     = new TextField();
    private final Button           btnNew     = new Button("New Category");
    private final TextField        nameField  = new TextField("Name");
    private final ComboBox<Category> parentCombo = new ComboBox<>("Parent Category");
    private final Button           save       = new Button("Save");
    private final Button           delete     = new Button("Delete");
    private final Button           clear      = new Button("Clear");
    private final Binder<Category> binder     = new Binder<>(Category.class);
    private final TreeData<Category>       treeData;
    private final TreeDataProvider<Category> dataProvider;
    private Category current; // the category being edited
    
    @Autowired
    public AdminCategoryView(StoreService service) {
        this.service = service;
        this.treeData     = new TreeData<>();
        this.dataProvider = new TreeDataProvider<>(treeData);
        tree.setDataProvider(dataProvider);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        
        // --- HEADER BAR: filter + "new" button ---
        filter.setPlaceholder("Search…");
        filter.setClearButtonVisible(true);
        
        filter.addValueChangeListener(e -> updateTree());
        btnNew.addClickListener(e -> clearForm());
        
        HorizontalLayout header = new HorizontalLayout(new H2("Manage Categories"), filter, btnNew);
        header.expand(filter);
        header.setWidthFull();
        add(header);
        
        // --- MAIN SPLIT LAYOUT ---
        SplitLayout split = new SplitLayout();
        split.setSizeFull();
        split.addToPrimary(buildTreeGrid());
        split.addToSecondary(buildForm());
        split.setSplitterPosition(60); // percent
        add(split);
    }
    
    private TreeGrid<Category> buildTreeGrid() {
        // hierarchy column
        tree.addHierarchyColumn(Category::getName).setHeader("Name").setAutoWidth(true);
        tree.addColumn(Category::getFullPath).setHeader("Full Path").setSortable(true).setAutoWidth(true);
        tree.setSizeFull();
        
        tree.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));
        return tree;
    }
    
    private FormLayout buildForm() {
        parentCombo.setItemLabelGenerator(Category::getFullPath);
        parentCombo.setClearButtonVisible(true);
        
        // bind name
        binder.forField(nameField)
        .asRequired("Name is required")
        .withValidator(new StringLengthValidator("1–50 chars", 1, 50))
        .bind(Category::getName, Category::setName);
        
        // bind parent
        binder.forField(parentCombo)
        .bind(Category::getParent, Category::setParent);
        
        // buttons
        save.addClickListener(e -> saveCategory());
        delete.addClickListener(e -> confirmDelete());
        clear.addClickListener(e -> clearForm());
        
        delete.setEnabled(false);
        
        FormLayout form = new FormLayout();
        form.add(nameField, parentCombo, new HorizontalLayout(save, delete, clear));
        form.setMaxWidth("400px");
        form.setResponsiveSteps(
                                new FormLayout.ResponsiveStep("0", 1),
                                new FormLayout.ResponsiveStep("500px", 2)
                                );
        return form;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        updateTree();
        clearForm();
    }
    
    private void updateTree() {
        // 1) load everything once
        List<Category> all = service.listAllCategories();
        
        // 2) apply text‐filter if needed
        String f = filter.getValue();
        List<Category> filtered;
        if (f == null || f.isBlank()) {
            filtered = all;
        } else {
            String fl = f.toLowerCase();
            filtered = all.stream()
            .filter(c -> c.getName().toLowerCase().contains(fl))
            .collect(Collectors.toList());
        }
        
        // 3) build parent→children map on the full list
        Map<Long, List<Category>> childrenMap = all.stream()
        .filter(c -> c.getParent() != null)
        .collect(Collectors.groupingBy(c -> c.getParent().getId()));
        
        // 4) clear & rebuild our single TreeData
        treeData.clear();
        filtered.stream()
        .filter(c -> c.getParent() == null)
        .forEach(root -> addNodeRecursively(treeData, root, childrenMap));
        
        // 5) tell Vaadin to refresh
        dataProvider.refreshAll();
        
        // 6) rebuild the parent‐combo as before
        List<Category> parents = new ArrayList<>(all);
        if (current != null) {
            Set<Category> forbidden = new HashSet<>();
            forbidden.add(current);
            collectDescendants(current, forbidden, childrenMap);
            parents.removeAll(forbidden);
        }
        parents.sort(Comparator.comparing(Category::getFullPath));
        parentCombo.setItems(parents);
    }
    
    private void addNodeRecursively(TreeData<Category> data,
                                    Category node,
                                    Map<Long, List<Category>> childrenMap) {
        data.addItem(node.getParent(), node);
        for (Category child : childrenMap.getOrDefault(node.getId(), Collections.emptyList())) {
            addNodeRecursively(data, child, childrenMap);
        }
    }
    
    private void collectDescendants(Category cat,
                                    Set<Category> collector,
                                    Map<Long, List<Category>> childrenMap) {
        for (Category child : childrenMap.getOrDefault(cat.getId(), Collections.emptyList())) {
            if (collector.add(child)) {
                collectDescendants(child, collector, childrenMap);
            }
        }
    }
    
    private void edit(Category c) {
        if (c == null) {
            clearForm();
            return;
        }
        this.current = service.findCategoryById(c.getId()).orElse(c);
        binder.readBean(current);
        delete.setEnabled(true);
        save.setText("Update");
        updateTree(); // to refresh parentCombo
    }
    
    private void saveCategory() {
        try {
            if (current == null) {
                current = new Category();
            }
            binder.writeBean(current);
            service.saveCategory(current);
            Notification.show("Saved", 2_000, Notification.Position.MIDDLE);
            clearForm();
            updateTree();
        } catch (ValidationException ex) {
            Notification.show("Please fix the errors", 2_000, Notification.Position.MIDDLE);
        }
    }
    
    private void confirmDelete() {
        if (current == null || current.getId() == null) {
            Notification.show("Select a category first", 2_000, Notification.Position.MIDDLE);
            return;
        }
        ConfirmDialog dlg = new ConfirmDialog(
                                              "Delete?",
                                              "Really delete “" + current.getFullPath() + "” and its subcategories?",
                                              "Delete", evt -> {
                                                  service.deleteCategoryById(current.getId());
                                                  Notification.show("Deleted", 2_000, Notification.Position.MIDDLE);
                                                  clearForm();
                                                  updateTree();
                                              },
                                              "Cancel", evt -> {}
                                              );
        dlg.open();
    }
    
    private void clearForm() {
        current = null;
        binder.readBean(new Category());
        tree.getSelectionModel().deselectAll();
        delete.setEnabled(false);
        save.setText("Save");
        updateTree();
    }
}

