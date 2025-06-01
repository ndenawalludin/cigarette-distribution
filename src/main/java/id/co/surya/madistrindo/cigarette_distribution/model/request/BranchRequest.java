package id.co.surya.madistrindo.cigarette_distribution.model.request;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record BranchRequest(
        String name,
        String address,
        String contact,
        String region
) {}
