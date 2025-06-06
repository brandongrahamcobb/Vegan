package com.brandongcobb.vegan.store.api.dto;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long orderId,
    Long veganId,
    Instant orderDate,
    String status,
    List<OrderLineResponse> items
) {}