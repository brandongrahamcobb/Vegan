package com.brandongcobb.vegan.store.service.impl;

import com.brandongcobb.vegan.store.api.dto.OrderLineRequest;
import com.brandongcobb.vegan.store.api.dto.OrderLineResponse;
import com.brandongcobb.vegan.store.api.dto.OrderResponse;
import com.brandongcobb.vegan.store.api.dto.PlaceOrderRequest;
import com.brandongcobb.vegan.store.domain.Vegan;
import com.brandongcobb.vegan.store.domain.Order;
import com.brandongcobb.vegan.store.domain.OrderLine;
import com.brandongcobb.vegan.store.domain.OrderStatus;
import com.brandongcobb.vegan.store.domain.Product;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.repo.OrderRepository;
import com.brandongcobb.vegan.store.repo.ProductRepository;
import com.brandongcobb.vegan.store.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final VeganRepository veganRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            VeganRepository veganRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.veganRepository = veganRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        Vegan vegan = veganRepository.findById(request.veganId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vegan not found"));
        List<OrderLine> lines = request.items().stream().map(item -> {
            Product product = productRepository.getProductByProductId(item.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found: " + item.productId()));
            if (product.getStock() < item.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - item.quantity());
            OrderLine line = new OrderLine(product, item.quantity(), product.getPrice());
            return line;
        }).collect(Collectors.toList());
        Order order = new Order(vegan, lines);
        Order saved = orderRepository.save(order);
        List<OrderLineResponse> itemResponses = saved.getOrderLines().stream()
                .map(line -> new OrderLineResponse(
                        line.getProduct().getId(),
                        line.getProduct().getName(),
                        line.getQuantity(),
                        line.getPrice()))
                .collect(Collectors.toList());
        return new OrderResponse(
                saved.getId(),
                saved.getVegan().getId(),
                saved.getOrderDate(),
                saved.getStatus().name(),
                itemResponses
        );
    }

    @Override
    public void cancelOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.getOrderLines().forEach(line -> {
            Product product = line.getProduct();
            product.setStock(product.getStock() + line.getQuantity());
        });
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long veganId) {
        if (!veganRepository.existsById(veganId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vegan not found");
        }
        return orderRepository.getVeganByUserId(veganId).stream()
                .map(order -> {
                    List<OrderLineResponse> items = order.getOrderLines().stream()
                            .map(line -> new OrderLineResponse(
                                    line.getProduct().getId(),
                                    line.getProduct().getName(),
                                    line.getQuantity(),
                                    line.getPrice()))
                            .collect(Collectors.toList());
                    return new OrderResponse(
                            order.getId(),
                            order.getVegan().getId(),
                            order.getOrderDate(),
                            order.getStatus().name(),
                            items
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        List<OrderLineResponse> items = order.getOrderLines().stream()
                .map(line -> new OrderLineResponse(
                        line.getProduct().getId(),
                        line.getProduct().getName(),
                        line.getQuantity(),
                        line.getPrice()))
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                order.getVegan().getId(),
                order.getOrderDate(),
                order.getStatus().name(),
                items
        );
    }
}
