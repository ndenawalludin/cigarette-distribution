package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.DistributionRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.ProductRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final DistributionRepository distributionRepository;
    private final StockRepository stockRepository;

    public int getTotalProduk() {
        return (int) productRepository.count();
    }

    public int getTotalCabang() {
        return (int) branchRepository.count();
    }

    public int getDistribusiHariIni() {
        return distributionRepository.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay()
        );
    }

    public int getTotalStok() {
        return stockRepository.sumAllQuantity();
    }

    public Map<String, Integer> getDistribusiMingguan() {
        Map<String, Integer> result = new LinkedHashMap<>();
        var today = LocalDate.now();

        // Start minggu hari Senin
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        for (int i = 0; i < 7; i++) {
            var date = startOfWeek.plusDays(i);
            var jumlah = distributionRepository.countByCreatedAtBetween(
                    date.atStartOfDay(),
                    date.plusDays(1).atStartOfDay()
            );

            String hari = switch (date.getDayOfWeek()) {
                case MONDAY -> "Sen";
                case TUESDAY -> "Sel";
                case WEDNESDAY -> "Rab";
                case THURSDAY -> "Kam";
                case FRIDAY -> "Jum";
                case SATURDAY -> "Sab";
                case SUNDAY -> "Min";
            };

            result.put(hari, jumlah);
        }

        return result;
    }

}
