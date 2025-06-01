package id.co.surya.madistrindo.cigarette_distribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.surya.madistrindo.cigarette_distribution.model.request.StockRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.request.StockRequestBuilder;
import id.co.surya.madistrindo.cigarette_distribution.model.response.StockResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.StockResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@ExtendWith(SpringExtension.class)
class StockControllerTest {

    @InjectMocks
    private StockController stockController;

    @Mock
    private StockService stockService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/stocks - Success")
    void getAllStocks() throws Exception {
        var stock1 = StockResponseBuilder.builder()
                .id(1L)
                .branchId(1L)
                .productId(1L)
                .quantity(100)
                .build();

        var stock2 = StockResponseBuilder.builder()
                .id(2L)
                .branchId(1L)
                .productId(2L)
                .quantity(200)
                .build();

        Mockito.when(stockService.getAll()).thenReturn(List.of(stock1, stock2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/stocks"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/stocks/branch/{branchId} - Success")
    void getStockByBranch() throws Exception {
        Long branchId = 1L;
        var stock = StockResponseBuilder.builder()
                .id(1L)
                .branchId(branchId)
                .productId(1L)
                .quantity(150)
                .build();

        Mockito.when(stockService.getStockByBranch(branchId)).thenReturn(List.of(stock));

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/stocks/branch/{branchId}", branchId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/stocks/branch/{branchId}/product/{productId} - Success")
    void getStockByBranchAndProduct() throws Exception {
        Long branchId = 1L;
        Long productId = 2L;
        var stock = StockResponseBuilder.builder()
                .id(1L)
                .branchId(branchId)
                .productId(productId)
                .quantity(120)
                .build();

        Mockito.when(stockService.getStockByBranchIdAndProductId(branchId, productId)).thenReturn(stock);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/stocks/branch/{branchId}/product/{productId}", branchId, productId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /cigarette-distribution/v1/stocks - Success")
    void updateStock() throws Exception {
        StockRequest request = StockRequestBuilder.builder()
                .branchId(1L)
                .productId(1L)
                .quantity(300)
                .build();

        StockResponse response = StockResponseBuilder.builder()
                .id(1L)
                .branchId(1L)
                .productId(1L)
                .quantity(300)
                .build();

        Mockito.when(stockService.updateStock(Mockito.any(StockRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/cigarette-distribution/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
