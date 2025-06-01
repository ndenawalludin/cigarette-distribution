package id.co.surya.madistrindo.cigarette_distribution.model.request;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record ProductRequest(
        String name,
        String category,
        String brand
) {}
