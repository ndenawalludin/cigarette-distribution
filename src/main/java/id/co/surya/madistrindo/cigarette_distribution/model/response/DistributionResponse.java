package id.co.surya.madistrindo.cigarette_distribution.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDateTime;

@RecordBuilder
public record DistributionResponse(
        Long id,
        Product product,
        Branch branchFrom,
        Branch branchTo,
        Integer quantity,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt
) {}
