package id.co.surya.madistrindo.cigarette_distribution.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.Serializable;

@RecordBuilder
public record BranchResponse(
        Long id,
        String name,
        String address,
        String contact,
        String region
) implements Serializable {}
