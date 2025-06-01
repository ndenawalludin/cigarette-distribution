package id.co.surya.madistrindo.cigarette_distribution.model.request.user;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record LoginRequest(
        String username,
        String password
) {}
