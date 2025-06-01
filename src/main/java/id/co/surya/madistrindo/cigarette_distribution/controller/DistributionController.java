package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.model.request.DistributionRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.DistributionResponse;
import id.co.surya.madistrindo.cigarette_distribution.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cigarette-distribution/v1/distributions")
@RequiredArgsConstructor
public class DistributionController {
    private final DistributionService distributionService;

    @GetMapping
    public List<DistributionResponse> getAllDistributions() {
        return distributionService.getAllDistributions();
    }

    @PostMapping
    public DistributionResponse create(@RequestBody DistributionRequest req) {
        return distributionService.createDistribution(req);
    }

    @PutMapping("/{id}/status")
    public DistributionResponse updateStatus(@PathVariable Long id, @RequestParam String status) {
        return distributionService.updateStatus(id, status);
    }

    @GetMapping("/exists-productid/{id}")
    public boolean existsByProductId(@PathVariable Long id) {
        return distributionService.existsByProductId(id);
    }

    @GetMapping("/exists-branchid/{id}")
    public boolean existsByBranchId(@PathVariable Long id) {
        return distributionService.existsByBranchId(id);
    }
}
