package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Stock;
import id.co.surya.madistrindo.cigarette_distribution.model.request.StockRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.StockResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.StockResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public List<StockResponse> getAll() {
        return stockRepository.findAllOrderByUpdatedAtDesc().stream()
                .map(dataStock -> StockResponseBuilder.builder()
                        .id(dataStock.getId())
                        .productId(dataStock.getProduct().getId())
                        .productName(dataStock.getProduct().getName())
                        .brand(dataStock.getProduct().getBrand())
                        .branchId(dataStock.getBranch().getId())
                        .branchName(dataStock.getBranch().getName())
                        .quantity(dataStock.getQuantity())
                        .build())
                .toList();
    }

    public List<StockResponse> getStockByBranch(Long branchId) {
        return stockRepository.findByBranchId(branchId).stream()
                .map(dataStock -> StockResponseBuilder.builder()
                        .id(dataStock.getId())
                        .productId(dataStock.getProduct().getId())
                        .productName(dataStock.getProduct().getName())
                        .brand(dataStock.getProduct().getBrand())
                        .branchId(dataStock.getBranch().getId())
                        .branchName(dataStock.getBranch().getName())
                        .quantity(dataStock.getQuantity())
                        .build())
                .toList();
    }

    public StockResponse getStockByBranchIdAndProductId(Long branchId, Long productId) {
        return stockRepository.findByBranchIdAndProductId(branchId, productId)
                .map(dataStock -> StockResponseBuilder.builder()
                        .id(dataStock.getId())
                        .productId(dataStock.getProduct().getId())
                        .productName(dataStock.getProduct().getName())
                        .brand(dataStock.getProduct().getBrand())
                        .branchId(dataStock.getBranch().getId())
                        .branchName(dataStock.getBranch().getName())
                        .quantity(dataStock.getQuantity())
                        .build())
                .orElse(null);
    }

    public StockResponse updateStock(StockRequest req) {
        var product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        var branch = branchRepository.findById(req.branchId())
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));

        var stock = stockRepository.findByBranchIdAndProductId(branch.getId(), product.getId())
                .orElse(new Stock(null, product, branch, 0, LocalDateTime.now()));

        stock.setQuantity(req.quantity());
        stock.setUpdatedAt(LocalDateTime.now());
        var saved = stockRepository.save(stock);

        return StockResponseBuilder.builder()
                .id(saved.getId())
                .productId(saved.getProduct().getId())
                .productName(saved.getProduct().getName())
                .brand(saved.getProduct().getBrand())
                .branchId(saved.getBranch().getId())
                .branchName(saved.getBranch().getName())
                .quantity(saved.getQuantity())
                .build();
    }

    @Transactional
    public void decreaseStock(Long branchId, Long productId, int quantity) {
        var stock = stockRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new NoSuchElementException("Stock not found for product " + productId + " in branch " + branchId));

        if (stock.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock in branch " + branchId);
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);
    }

    @Transactional
    public void increaseStock(Long branchId, Long productId, int quantity) {
        var stockOpt = stockRepository.findByBranchIdAndProductId(branchId, productId);

        if (stockOpt.isPresent()) {
            var stock = stockOpt.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            stockRepository.save(stock);
        } else {
            // Jika belum ada stoknya, buat baru
            var product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));

            var branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new NoSuchElementException("Branch not found"));

            var newStock = new Stock(null, product, branch, quantity,  LocalDateTime.now());
            stockRepository.save(newStock);

        }
    }
}
