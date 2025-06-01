package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Stock;
import id.co.surya.madistrindo.cigarette_distribution.model.request.ProductRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final StockRepository stockRepository;

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(dataProduct -> ProductResponseBuilder.builder()
                        .id(dataProduct.getId())
                        .name(dataProduct.getName())
                        .category(dataProduct.getCategory())
                        .brand(dataProduct.getBrand())
                        .build())
                .toList();
    }

    @Transactional
    public ProductResponse save(ProductRequest req) {
        var product = new Product(null, req.name(), req.category(), req.brand());
        var saved = productRepository.save(product);
        //initialize stock for all branch
        initializeStock(saved);
        return ProductResponseBuilder.builder()
                .id(saved.getId())
                .name(saved.getName())
                .category(saved.getCategory())
                .brand(saved.getBrand())
                .build();
    }

    private void initializeStock(Product product) {
        var constructNewStock = branchRepository.findAll().stream()
                .map(branch -> Stock.builder()
                        .product(product)
                        .branch(branch)
                        .quantity(0)
                        .build())
                .toList();
        if (constructNewStock.isEmpty()) {
            throw new NoSuchElementException("Branch not found");
        }
        stockRepository.saveAll(constructNewStock);
    }

    public ProductResponse update(Long id, ProductRequest req) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setName(req.name());
        product.setCategory(req.category());
        product.setBrand(req.brand());
        var updated = productRepository.save(product);
        return ProductResponseBuilder.builder()
                .id(updated.getId())
                .name(updated.getName())
                .category(updated.getCategory())
                .brand(updated.getBrand())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        //must delete stock and distribution if already exist
        productRepository.findById(id).ifPresent(stockRepository::deleteAllByProduct);

        productRepository.deleteById(id);
    }
}
