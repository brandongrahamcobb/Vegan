package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.api.dto.OrderResponse;
import com.brandongcobb.vegan.store.api.dto.PlaceOrderRequest;
import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(PlaceOrderRequest request);
    void cancelOrder(Long orderId);
    List<OrderResponse> listVeganOrders(Long veganId);
    OrderResponse getOrder(Long orderId);
}
