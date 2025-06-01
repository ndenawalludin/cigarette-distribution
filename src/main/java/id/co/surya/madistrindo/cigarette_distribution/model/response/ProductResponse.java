package id.co.surya.madistrindo.cigarette_distribution.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record ProductResponse(
        Long id,
        String name,
        String category,
        String brand
) {}
