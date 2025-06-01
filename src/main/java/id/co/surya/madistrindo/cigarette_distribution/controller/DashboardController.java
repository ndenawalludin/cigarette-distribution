package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/cigarette-distribution/v1/dashboard", produces = "application/json")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/total-produk")
    public int getTotalProduk() {
        return dashboardService.getTotalProduk();
    }

    @GetMapping("/total-cabang")
    public int getTotalCabang() {
        return dashboardService.getTotalCabang();
    }

    @GetMapping("/distribusi-hari-ini")
    public int getDistribusiHariIni() {
        return dashboardService.getDistribusiHariIni();
    }

    @GetMapping("/total-stok")
    public int getTotalStok() {
        return dashboardService.getTotalStok();
    }

    @GetMapping("/distribusi-mingguan")
    public Map<String, Integer> getDistribusiMingguan() {
        return dashboardService.getDistribusiMingguan();
    }

}
