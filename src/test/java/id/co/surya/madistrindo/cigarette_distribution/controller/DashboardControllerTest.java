package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

@ExtendWith(SpringExtension.class)
class DashboardControllerTest {

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private DashboardService dashboardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/dashboard/total-produk - Success")
    void getTotalProduk() throws Exception {
        Mockito.when(dashboardService.getTotalProduk()).thenReturn(42);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/dashboard/total-produk"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("42"));
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/dashboard/total-cabang - Success")
    void getTotalCabang() throws Exception {
        Mockito.when(dashboardService.getTotalCabang()).thenReturn(15);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/dashboard/total-cabang"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("15"));
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/dashboard/distribusi-hari-ini - Success")
    void getDistribusiHariIni() throws Exception {
        Mockito.when(dashboardService.getDistribusiHariIni()).thenReturn(8);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/dashboard/distribusi-hari-ini"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("8"));
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/dashboard/total-stok - Success")
    void getTotalStok() throws Exception {
        Mockito.when(dashboardService.getTotalStok()).thenReturn(100);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/dashboard/total-stok"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("100"));
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/dashboard/distribusi-mingguan - Success")
    void getDistribusiMingguan() throws Exception {
        Map<String, Integer> distribusi = Map.of(
                "Monday", 10,
                "Tuesday", 15,
                "Wednesday", 20
        );

        Mockito.when(dashboardService.getDistribusiMingguan()).thenReturn(distribusi);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/dashboard/distribusi-mingguan"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
