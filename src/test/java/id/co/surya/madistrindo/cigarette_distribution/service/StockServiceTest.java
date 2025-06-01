package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Stock;
import id.co.surya.madistrindo.cigarette_distribution.model.request.StockRequest;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    private Product product;
    private Branch branch;
    private Stock stock;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Surya 12", "Filter", "Gudang Garam");
        branch = new Branch(1L, "Cabang A", "Jl. Sudirman", "0212345", "Jakarta");
        stock = new Stock(1L, product, branch, 100, LocalDateTime.now());

        Mockito.reset(stockRepository, productRepository, branchRepository);
    }

    @Test
    @DisplayName("Get all stocks - should return mapped list")
    void getAllStocks() {
        Stock anotherStock = new Stock(2L, product, branch, 50, LocalDateTime.now());
        Mockito.when(stockRepository.findAllOrderByUpdatedAtDesc()).thenReturn(List.of(stock, anotherStock));

        var result = stockService.getAll();

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(stock.getId(), result.get(0).id());
        Assertions.assertEquals(stock.getProduct().getName(), result.get(0).productName());
        Assertions.assertEquals(stock.getBranch().getName(), result.get(0).branchName());

        Assertions.assertEquals(anotherStock.getId(), result.get(1).id());
        Assertions.assertEquals(anotherStock.getQuantity(), result.get(1).quantity());
    }

    @Test
    @DisplayName("Get stocks by branch - should return mapped list")
    void getStockByBranch() {
        Stock stock1 = new Stock(3L, product, branch, 75, LocalDateTime.now());
        Mockito.when(stockRepository.findByBranchId(1L)).thenReturn(List.of(stock1));

        var result = stockService.getStockByBranch(1L);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(stock1.getId(), result.get(0).id());
        Assertions.assertEquals(stock1.getProduct().getName(), result.get(0).productName());
        Assertions.assertEquals(stock1.getQuantity(), result.get(0).quantity());
        Assertions.assertEquals(stock1.getBranch().getId(), result.get(0).branchId());
    }


    @Test
    @DisplayName("Get stock by branch and product - should return correct response")
    void getStockByBranchIdAndProductId() {
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));

        var result = stockService.getStockByBranchIdAndProductId(1L, 1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.productId());
        Assertions.assertEquals(100, result.quantity());
    }

    @Test
    @DisplayName("Get stock by branch and product - should return null if not found")
    void getStockByBranchIdAndProductId_notFound() {
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        var result = stockService.getStockByBranchIdAndProductId(1L, 1L);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Update stock - should update existing stock")
    void updateStock_existing() {
        StockRequest request = new StockRequest(1L, 1L, 150);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenAnswer(i -> i.getArgument(0));

        var result = stockService.updateStock(request);

        Assertions.assertEquals(150, result.quantity());
        Mockito.verify(stockRepository).save(Mockito.any(Stock.class));
    }

    @Test
    @DisplayName("Update stock - should create new stock if not exists")
    void updateStock_new() {
        StockRequest request = new StockRequest(1L, 1L, 50);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenAnswer(i -> i.getArgument(0));

        var result = stockService.updateStock(request);

        Assertions.assertEquals(50, result.quantity());
        Mockito.verify(stockRepository).save(Mockito.any(Stock.class));
    }

    @Test
    @DisplayName("Increase stock - existing stock")
    void increaseStock_existing() {
        stock.setQuantity(70);
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));

        stockService.increaseStock(1L, 1L, 30);

        Mockito.verify(stockRepository).save(Mockito.argThat(s -> s.getQuantity() == 100));
    }

    @Test
    @DisplayName("Increase stock - new stock if not exists")
    void increaseStock_new() {
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        stockService.increaseStock(1L, 1L, 40);

        Mockito.verify(stockRepository).save(Mockito.argThat(s -> s.getQuantity() == 40));
    }

    @Test
    @DisplayName("Decrease stock - success if sufficient stock")
    void decreaseStock_success() {
        stock.setQuantity(100);
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));

        stockService.decreaseStock(1L, 1L, 60);

        Mockito.verify(stockRepository).save(Mockito.argThat(s -> s.getQuantity() == 40));
    }

    @Test
    @DisplayName("Decrease stock - throw if insufficient")
    void decreaseStock_insufficient() {
        stock.setQuantity(20);
        Mockito.when(stockRepository.findByBranchIdAndProductId(1L, 1L)).thenReturn(Optional.of(stock));

        Assertions.assertThrows(IllegalArgumentException.class, () -> stockService.decreaseStock(1L, 1L, 50));
    }
}
