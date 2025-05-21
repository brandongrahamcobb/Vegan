package com.brandongcobb.vegan.store.api.dto;

public record VeganResponse(
    Long id,
    String firstName,
    String lastName,
    String email
) {}