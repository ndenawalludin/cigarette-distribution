package id.co.surya.madistrindo.cigarette_distribution.model.request;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record DistributionRequest(
        Long productId,
        Long branchFromId,
        Long branchToId,
        Integer quantity
) {}
