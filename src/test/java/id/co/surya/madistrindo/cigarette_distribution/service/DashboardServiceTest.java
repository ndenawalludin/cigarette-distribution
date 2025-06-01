package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.DistributionRepository;
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

import java.time.LocalDate;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private DistributionRepository distributionRepository;

    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(productRepository, branchRepository, distributionRepository, stockRepository);
    }

    @Test
    @DisplayName("Get total produk - should return correct count")
    void getTotalProduk() {
        Mockito.when(productRepository.count()).thenReturn(5L);

        int result = dashboardService.getTotalProduk();

        Assertions.assertEquals(5, result);
        Mockito.verify(productRepository, Mockito.times(1)).count();
    }

    @Test
    @DisplayName("Get total cabang - should return correct count")
    void getTotalCabang() {
        Mockito.when(branchRepository.count()).thenReturn(3L);

        int result = dashboardService.getTotalCabang();

        Assertions.assertEquals(3, result);
        Mockito.verify(branchRepository, Mockito.times(1)).count();
    }

    @Test
    @DisplayName("Get distribusi hari ini - should return correct count")
    void getDistribusiHariIni() {
        var todayStart = LocalDate.now().atStartOfDay();
        var tomorrowStart = LocalDate.now().plusDays(1).atStartOfDay();

        Mockito.when(distributionRepository.countByCreatedAtBetween(todayStart, tomorrowStart)).thenReturn(7);

        int result = dashboardService.getDistribusiHariIni();

        Assertions.assertEquals(7, result);
        Mockito.verify(distributionRepository, Mockito.times(1))
                .countByCreatedAtBetween(todayStart, tomorrowStart);
    }

    @Test
    @DisplayName("Get total stok - should return correct sum")
    void getTotalStok() {
        Mockito.when(stockRepository.sumAllQuantity()).thenReturn(1234);

        int result = dashboardService.getTotalStok();

        Assertions.assertEquals(1234, result);
        Mockito.verify(stockRepository, Mockito.times(1)).sumAllQuantity();
    }

    @Test
    @DisplayName("Get distribusi mingguan - should return map with 7 entries")
    void getDistribusiMingguan() {
        var now = LocalDate.now();
        var startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1L);

        for (int i = 0; i < 7; i++) {
            var date = startOfWeek.plusDays(i);
            var start = date.atStartOfDay();
            var end = date.plusDays(1).atStartOfDay();
            Mockito.when(distributionRepository.countByCreatedAtBetween(start, end)).thenReturn(i);
        }

        Map<String, Integer> result = dashboardService.getDistribusiMingguan();

        Assertions.assertEquals(7, result.size());
        Assertions.assertTrue(result.containsKey("Sen"));
        Assertions.assertTrue(result.containsKey("Min"));
        Mockito.verify(distributionRepository, Mockito.times(7)).countByCreatedAtBetween(Mockito.any(), Mockito.any());
    }
}
