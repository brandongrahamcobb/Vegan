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
    
    public default void syncCurrentUser(VeganRepository veganRepo, AvatarItem avatarItem) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            veganRepo.findByEmail(auth.getName()).ifPresent(v -> {
                String full = v.getFirstName() + " " + v.getLastName();
                VaadinSession.getCurrent().setAttribute("userId", v.getId());
                Avatar avatar = new Avatar(full);
                avatarItem.setAvatar(avatar);
                avatarItem.setHeading(full);
                avatarItem.setDescription(v.getEmail());
            });
        }
    }
}
