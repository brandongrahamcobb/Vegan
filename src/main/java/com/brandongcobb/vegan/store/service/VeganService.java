package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.api.dto.AuthResponse;
import com.brandongcobb.vegan.store.api.dto.VeganLoginRequest;
import com.brandongcobb.vegan.store.api.dto.VeganRegistrationRequest;
import com.brandongcobb.vegan.store.api.dto.VeganResponse;

public interface VeganService {
    VeganResponse register(VeganRegistrationRequest request);
    AuthResponse login(VeganLoginRequest request);
}