package id.co.surya.madistrindo.cigarette_distribution.model.response.user;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record LoginResponse(
        String token,
        String username,
        String role
) {}
