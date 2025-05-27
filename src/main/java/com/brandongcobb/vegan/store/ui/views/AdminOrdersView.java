//

//  AdminOrdersView.java
//  
//
//  Created by Brandon Cobb on 5/20/25.
//
package com.brandongcobb.vegan.store.ui.views;

import com.vaadin.flow.server.VaadinSession;
import com.brandongcobb.vegan.store.api.dto.OrderResponse;
import com.brandongcobb.vegan.store.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import com.brandongcobb.vegan.store.ui.layouts.*;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "admin/orders", layout = AdminLayout.class)
@PageTitle("Order Management | The Vyrtuous Project")
public class AdminOrdersView extends VerticalLayout {
    
    private final OrderService orderService;
    private Grid<OrderResponse> grid;
    private TextField searchField;
    private MultiSelectComboBox<String> statusFilter;
    private DatePicker dateRangeStart;
    private DatePicker dateRangeEnd;
    private List<OrderResponse> allOrders;

    @Autowired
    public AdminOrdersView(OrderService orderService) {
        this.orderService = orderService;
        setSizeFull();
        setPadding(true);
        
        // Header
        H2 header = new H2("Order Management Dashboard");
        add(header);

        // Filters section
        HorizontalLayout filters = new HorizontalLayout();
        filters.setSpacing(true);
        filters.setAlignItems(Alignment.BASELINE);

        // Search
        searchField = new TextField();
        searchField.setPlaceholder("Search by order ID");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(e -> refreshGrid());

        // Status filter
        statusFilter = new MultiSelectComboBox<>("Status");
        statusFilter.setItems("Pending", "Processing", "Shipped", "Delivered", "Cancelled");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        // Date range
        dateRangeStart = new DatePicker("From");
        dateRangeEnd = new DatePicker("To");
        dateRangeEnd.setValue(LocalDate.now());
        dateRangeStart.addValueChangeListener(e -> refreshGrid());
        dateRangeEnd.addValueChangeListener(e -> refreshGrid());

        filters.add(searchField, statusFilter, dateRangeStart, dateRangeEnd);
        add(filters);

        // Grid
        grid = new Grid<>(OrderResponse.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        
        // Configure grid columns
        grid.addColumn(order -> order.orderId()).setHeader("Order ID");
        grid.addColumn(order -> order.veganId()).setHeader("Customer ID");
        grid.addColumn(order -> order.status()).setHeader("Status");
        grid.addColumn(order -> order.orderDate().atZone(ZoneId.systemDefault()).toLocalDate()).setHeader("Date");
        
        // Actions column
        grid.addComponentColumn(order -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button viewBtn = new Button("View", e -> viewOrderDetails(order));
            viewBtn.setIcon(VaadinIcon.EYE.create());
            
            Button cancelBtn = new Button("Cancel", e -> cancelOrder(order));
            cancelBtn.setIcon(VaadinIcon.TRASH.create());
            
            actions.add(viewBtn, cancelBtn);
            return actions;
        }).setHeader("Actions");

        add(grid);
        loadOrders();
    }

    private void loadOrders() {
        // Get user ID from VaadinSession
        // For admin view, you might want to list all orders or filter by admin's scope,
        // but for now, keeping it as is based on previous context.
        // If this is truly an admin view, it should probably not filter by current user's veganId.
        // Assuming for now that 'userId' might be used for some admin-specific filtering or logging.
        // If you want to list ALL orders for admin, you'd call orderService.listAllOrders() (if it exists)
        // or remove the veganId filter from orderService.listVeganOrders.
        Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
        if (userId != null) {
            allOrders = orderService.listVeganOrders(userId); // This will only show orders for the logged-in admin if they are also a 'vegan'
            refreshGrid();
        } else {
            // Handle case where admin is not a 'vegan' user or userId is not set
            // For a true admin view, you'd likely fetch all orders or orders based on admin roles/permissions
            Notification.show("Admin user ID not found in session. Cannot load orders.", 3000, Notification.Position.MIDDLE);
            allOrders = List.of(); // Empty list
            refreshGrid();
        }
    }

    private void refreshGrid() {
        List<OrderResponse> filteredOrders = allOrders.stream()
            .filter(order -> matchesSearch(order))
            .filter(order -> matchesStatus(order))
            .filter(order -> matchesDateRange(order))
            .collect(Collectors.toList());
        
        grid.setItems(filteredOrders);
    }

    private boolean matchesSearch(OrderResponse order) {
        String searchTerm = searchField.getValue().toLowerCase();
        return searchTerm.isEmpty() ||
               order.orderId().toString().contains(searchTerm);
    }

    private boolean matchesStatus(OrderResponse order) {
        if (statusFilter.isEmpty()) return true;
        return statusFilter.getValue().contains(order.status());
    }

    private boolean matchesDateRange(OrderResponse order) {
        LocalDate orderDate = order.orderDate().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = dateRangeStart.getValue();
        LocalDate endDate = dateRangeEnd.getValue();

        if (startDate == null && endDate == null) return true;
        if (startDate != null && endDate != null) {
            return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
        }
        if (startDate != null) {
            return !orderDate.isBefore(startDate);
        }
        return !orderDate.isAfter(endDate);
    }

    private void viewOrderDetails(OrderResponse order) {
        Notification.show("Viewing order details for " + order.orderId());
    }

    private void cancelOrder(OrderResponse order) {
        try {
            orderService.cancelOrder(order.orderId());
            Notification.show("Order cancelled successfully");
            loadOrders(); // Reload all orders to reflect changes
        } catch (Exception e) {
            Notification.show("Failed to cancel order: " + e.getMessage());
        }
    }
}
