package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Distribution;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import id.co.surya.madistrindo.cigarette_distribution.model.request.DistributionRequest;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.DistributionRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DistributionServiceTest {

    @InjectMocks
    private DistributionService distributionService;

    @Mock
    private DistributionRepository distributionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private StockService stockService;

    private Product product;
    private Branch branchFrom;
    private Branch branchTo;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Rokok A", "Filter", "Gudang Garam");
        branchFrom = new Branch(1L, "Jakarta", "Jl. Mawar", "123", "DKI");
        branchTo = new Branch(2L, "Bandung", "Jl. Melati", "456", "Jabar");

        Mockito.reset(distributionRepository, productRepository, branchRepository, stockService);
    }

    @Test
    @DisplayName("Get all distributions - should return list of DistributionResponse")
    void getAllDistributions() {
        var dist = new Distribution(1L, product, branchFrom, branchTo, 100, "PROSES", LocalDateTime.now(), "SYS", null, "SYS");

        Mockito.when(distributionRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(dist));

        var result = distributionService.getAllDistributions();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("PROSES", result.get(0).status());
    }

    @Test
    @DisplayName("Create distribution - should save and return DistributionResponse")
    void createDistribution() {
        DistributionRequest request = new DistributionRequest(product.getId(), branchFrom.getId(), branchTo.getId(), 50);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(branchRepository.findById(branchFrom.getId())).thenReturn(Optional.of(branchFrom));
        Mockito.when(branchRepository.findById(branchTo.getId())).thenReturn(Optional.of(branchTo));

        Distribution saved = new Distribution(1L, product, branchFrom, branchTo, 50, "PROSES", LocalDateTime.now(), "SYSTEM", null, "SYSTEM");

        Mockito.when(distributionRepository.save(Mockito.any(Distribution.class))).thenReturn(saved);

        var result = distributionService.createDistribution(request);

        Assertions.assertEquals(50, result.quantity());
        Assertions.assertEquals("PROSES", result.status());
        Mockito.verify(stockService).decreaseStock(branchFrom.getId(), product.getId(), 50);
        Mockito.verify(stockService).increaseStock(branchTo.getId(), product.getId(), 50);
    }

    @Test
    @DisplayName("Create distribution - should throw exception if product not found")
    void createDistribution_productNotFound() {
        DistributionRequest request = new DistributionRequest(999L, 1L, 2L, 10);
        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> distributionService.createDistribution(request));
    }

    @Test
    @DisplayName("Update status - should update and return updated response")
    void updateStatus() {
        Distribution existing = new Distribution(1L, product, branchFrom, branchTo, 100, "PROSES", LocalDateTime.now(), "SYS", null, "SYS");

        Mockito.when(distributionRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(distributionRepository.save(Mockito.any(Distribution.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = distributionService.updateStatus(1L, "SELESAI");

        Assertions.assertEquals("SELESAI", result.status());
        Mockito.verify(distributionRepository).save(Mockito.any(Distribution.class));
    }

    @Test
    @DisplayName("Update status - should throw if not found")
    void updateStatus_notFound() {
        Mockito.when(distributionRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> distributionService.updateStatus(99L, "SELESAI"));
    }

    @Test
    @DisplayName("Exists by productId - should return true")
    void existsByProductId() {
        Mockito.when(distributionRepository.existsByProductId(1L)).thenReturn(true);

        boolean result = distributionService.existsByProductId(1L);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Exists by branchId - should return true if exists in either from or to")
    void existsByBranchId() {
        Mockito.when(distributionRepository.existsByBranchFromId(1L)).thenReturn(false);
        Mockito.when(distributionRepository.existsByBranchToId(1L)).thenReturn(true);

        boolean result = distributionService.existsByBranchId(1L);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Exists by branchId - should return false if not used")
    void existsByBranchId_false() {
        Mockito.when(distributionRepository.existsByBranchFromId(1L)).thenReturn(false);
        Mockito.when(distributionRepository.existsByBranchToId(1L)).thenReturn(false);

        boolean result = distributionService.existsByBranchId(1L);

        Assertions.assertFalse(result);
    }
}
