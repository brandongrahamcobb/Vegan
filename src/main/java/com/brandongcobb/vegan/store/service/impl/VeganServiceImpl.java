package com.brandongcobb.vegan.store.service.impl;

import com.brandongcobb.vegan.store.api.dto.AuthResponse;
import com.brandongcobb.vegan.store.api.dto.VeganLoginRequest;
import com.brandongcobb.vegan.store.api.dto.VeganRegistrationRequest;
import com.brandongcobb.vegan.store.api.dto.VeganResponse;
import com.brandongcobb.vegan.store.domain.Vegan;
import com.brandongcobb.vegan.store.repo.VeganRepository;
import com.brandongcobb.vegan.store.service.VeganService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import com.brandongcobb.metadata.*;
import com.brandongcobb.vegan.utils.handlers.*;


@Service
public class VeganServiceImpl implements VeganService {

    private final VeganRepository veganRepository;
    private final PasswordEncoder passwordEncoder;
    private static final MetadataType<String> STRING = new MetadataString();
    private static final MetadataType<Integer> INTEGER = new MetadataInteger();
    private static final MetadataType<Double> DOUBLE = new MetadataDouble();
    private static final MetadataType<Float> FLOAT = new MetadataFloat();
    private static final MetadataType<Boolean> BOOLEAN = new MetadataBoolean();
    private static final MetadataType<Map<String, Object>> MAP = new MetadataMap();
    private static final MetadataType<List<String>> LIST = new MetadataList(STRING);
    
    public VeganServiceImpl(VeganRepository veganRepository, PasswordEncoder passwordEncoder) {
        this.veganRepository = veganRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void ensureAdminUserExists() {
        String defaultAdminEmail = "admin@admin.com";

        if (!veganRepository.existsByEmail(defaultAdminEmail)) {
            ConfigManager.completeGetInstance()
                .thenCompose(instance -> instance.completeGetConfigContainer()
                    .thenApply(container -> {
                        MetadataKey<String> adminPasswordKey = new MetadataKey<>("ADMIN_PASSWORD", STRING); // DEPRECATED: Use Metadata.STRING;
                        MetadataKey<String> adminEmailKey = new MetadataKey<>("ADMIN_EMAIL", STRING); // DEPRECATED: Use Metadata.STRING;

                        String password = container.get(adminPasswordKey);
                        String email = container.get(adminEmailKey);

                        String finalEmail = (email != null && !email.isBlank()) ? email : defaultAdminEmail;
                        String finalPassword = (password != null && !password.isBlank()) ? password : "admin"; // fallback

                        String encodedPassword = passwordEncoder.encode(finalPassword);

                        Vegan admin = new Vegan(
                            "Admin",
                            "User",
                            finalEmail,
                            encodedPassword
                        );
                        admin.setAdmin(true);
                        return admin;
                    })
                )
                .thenAccept(admin -> {
                    veganRepository.save(admin);
                    System.out.println("✅ Admin user created: " + admin.getEmail());
                })
                .exceptionally(ex -> {
                    System.err.println("❌ Failed to create admin user: " + ex.getMessage());
                    return null;
                });
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }
    
    @Override
    public VeganResponse register(VeganRegistrationRequest request) {
        if (veganRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        Vegan vegan = new Vegan(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword
        );
        Vegan saved = veganRepository.save(vegan);
        return new VeganResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail());
    }

    @Override
    public AuthResponse login(VeganLoginRequest request) {
        Vegan vegan = veganRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), vegan.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = UUID.randomUUID().toString();
        return new AuthResponse(token);
    }
}
