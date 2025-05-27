package com.brandongcobb.vegan.store.service;

import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.ui.components.AvatarItem;
import com.brandongcobb.vegan.store.api.dto.AuthResponse;
import com.brandongcobb.vegan.store.api.dto.VeganLoginRequest;
import com.brandongcobb.vegan.store.api.dto.VeganRegistrationRequest;
import com.brandongcobb.vegan.store.api.dto.VeganResponse;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface VeganService {
    VeganResponse register(VeganRegistrationRequest request);
    AuthResponse login(VeganLoginRequest request);
    
    // The syncCurrentUser method has been removed from the service interface
    // as it couples the service to UI components and Vaadin session.
    // This logic should be handled in the UI layer where AvatarItem and VaadinSession are relevant.
}
