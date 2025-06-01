package id.co.surya.madistrindo.cigarette_distribution.model.request;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record StockRequest(
        Long productId,
        Long branchId,
        Integer quantity
) {}
