package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Distribution;
import id.co.surya.madistrindo.cigarette_distribution.model.request.DistributionRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.DistributionResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.DistributionResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.DistributionRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DistributionService {
    private final DistributionRepository distributionRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final StockService stockService;

    public static final String SYSTEM = "SYSTEM";

    public List<DistributionResponse> getAllDistributions() {
        return distributionRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(distribution -> DistributionResponseBuilder.builder()
                        .id(distribution.getId())
                        .product(distribution.getProduct())
                        .branchFrom(distribution.getBranchFrom())
                        .branchTo(distribution.getBranchTo())
                        .quantity(distribution.getQuantity())
                        .status(distribution.getStatus())
                        .createdAt(distribution.getCreatedAt())
                        .build())
                .toList();
    }

    public DistributionResponse createDistribution(DistributionRequest req) {
        var product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        var branchFrom = branchRepository.findById(req.branchFromId())
                .orElseThrow(() -> new NoSuchElementException("Origin branch not found"));
        var branchTo = branchRepository.findById(req.branchToId())
                .orElseThrow(() -> new NoSuchElementException("Destination branch not found"));

        stockService.decreaseStock(branchFrom.getId(), product.getId(), req.quantity());

        stockService.increaseStock(branchTo.getId(), product.getId(), req.quantity());

        var dist = new Distribution(null, product, branchFrom, branchTo, req.quantity(), "PROSES", LocalDateTime.now(), SYSTEM, null, SYSTEM);
        var saved = distributionRepository.save(dist);

        return DistributionResponseBuilder.builder()
                .id(saved.getId())
                .product(saved.getProduct())
                .branchFrom(saved.getBranchFrom())
                .branchTo(saved.getBranchTo())
                .quantity(saved.getQuantity())
                .status(saved.getStatus())
                .build();
    }

    public DistributionResponse updateStatus(Long id, String status) {
        var dist = distributionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Distribution not found"));
        dist.setStatus(status);
        dist.setUpdatedAt(LocalDateTime.now());
        dist.setUpdatedBy(SYSTEM);
        var updated = distributionRepository.save(dist);

        return DistributionResponseBuilder.builder()
                .id(updated.getId())
                .product(updated.getProduct())
                .branchFrom(updated.getBranchFrom())
                .branchTo(updated.getBranchTo())
                .quantity(updated.getQuantity())
                .status(updated.getStatus())
                .build();
    }

    public boolean existsByProductId(Long productId) {
        return distributionRepository.existsByProductId(productId);
    }

    public boolean existsByBranchId(Long branchId) {
        return distributionRepository.existsByBranchFromId(branchId) || distributionRepository.existsByBranchToId(branchId);
    }
}
