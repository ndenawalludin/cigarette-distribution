package id.co.surya.madistrindo.cigarette_distribution.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record StockResponse(
        Long id,
        Long productId,
        String productName,
        String brand,
        Long branchId,
        String branchName,
        Integer quantity
) {}
