package id.co.surya.madistrindo.cigarette_distribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.surya.madistrindo.cigarette_distribution.model.request.DistributionRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.request.DistributionRequestBuilder;
import id.co.surya.madistrindo.cigarette_distribution.model.response.DistributionResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.DistributionResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.service.DistributionService;
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
class DistributionControllerTest {

    @InjectMocks
    private DistributionController distributionController;

    @Mock
    private DistributionService distributionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(distributionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/distributions - Success")
    void getAllDistributions() throws Exception {
        var response1 = DistributionResponseBuilder.builder()
                .id(1L)
                .status("PENDING")
                .build();
        var response2 = DistributionResponseBuilder.builder()
                .id(2L)
                .status("COMPLETED")
                .build();

        Mockito.when(distributionService.getAllDistributions()).thenReturn(List.of(response1, response2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/distributions"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /cigarette-distribution/v1/distributions - Success")
    void createDistribution() throws Exception {
        DistributionRequest request = DistributionRequestBuilder.builder()
                .productId(1L)
                .branchFromId(1L)
                .branchToId(2L)
                .quantity(10)
                .build();

        DistributionResponse response = DistributionResponseBuilder.builder()
                .id(1L)
                .status("PENDING")
                .build();

        Mockito.when(distributionService.createDistribution(Mockito.any(DistributionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/cigarette-distribution/v1/distributions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /cigarette-distribution/v1/distributions/{id}/status - Success")
    void updateStatus() throws Exception {
        Long id = 1L;
        String status = "COMPLETED";

        DistributionResponse response = DistributionResponseBuilder.builder()
                .id(id)
                .status(status)
                .build();

        Mockito.when(distributionService.updateStatus(id, status)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/cigarette-distribution/v1/distributions/{id}/status", id)
                        .param("status", status))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/distributions/exists-productid/{id} - Success")
    void existsByProductId() throws Exception {
        Long productId = 5L;

        Mockito.when(distributionService.existsByProductId(productId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/distributions/exists-productid/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/distributions/exists-branchid/{id} - Success")
    void existsByBranchId() throws Exception {
        Long branchId = 10L;

        Mockito.when(distributionService.existsByBranchId(branchId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/distributions/exists-branchid/{id}", branchId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
