package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.annotation.ScopedProxyMode;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {
    private final Map<Product, Integer> cart = new LinkedHashMap<>();

    public void addToCart(Product product, int qty) {
        if (qty <= 0) {
            removeFromCart(product); // Remove if quantity is zero or less
            return;
        }
        cart.merge(product, qty, Integer::sum);
    }

    public void updateQuantity(Product product, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(product);
        } else {
            cart.put(product, newQuantity);
        }
    }

    public void removeFromCart(Product product) {
        cart.remove(product);
    }

    public void clear() {
        cart.clear();
    }

    public Map<Product, Integer> getCartItems() {
        return Collections.unmodifiableMap(cart);
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public int getTotalQuantity() {
        return cart.values().stream().mapToInt(i -> i).sum();
    }
}
