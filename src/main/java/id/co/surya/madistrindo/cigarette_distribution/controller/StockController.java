package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.model.request.StockRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.StockResponse;
import id.co.surya.madistrindo.cigarette_distribution.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cigarette-distribution/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public List<StockResponse> getAll() {
        return stockService.getAll();
    }

    @GetMapping("/branch/{branchId}")
    public List<StockResponse> getStockByBranch(@PathVariable Long branchId) {
        return stockService.getStockByBranch(branchId);
    }

    @GetMapping("/branch/{branchId}/product/{productId}")
    public StockResponse getStockByBranchIdAndProductId(@PathVariable Long branchId, @PathVariable Long productId) {
        return stockService.getStockByBranchIdAndProductId(branchId, productId);
    }

    @PutMapping
    public StockResponse updateStock(@RequestBody StockRequest req) {
        return stockService.updateStock(req);
    }
}
