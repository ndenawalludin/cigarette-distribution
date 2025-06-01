package id.co.surya.madistrindo.cigarette_distribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.surya.madistrindo.cigarette_distribution.model.request.BranchRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.request.BranchRequestBuilder;
import id.co.surya.madistrindo.cigarette_distribution.model.response.BranchResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.service.BranchService;
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
class BranchControllerTest {

    @InjectMocks
    private BranchController branchController;

    @Mock
    private BranchService branchService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(branchController).build();
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/branches - Success")
    void getAllBranches() throws Exception {
        var response1 = BranchResponseBuilder.builder().id(1L).name("Branch A").address("Address A").build();
        var response2 = BranchResponseBuilder.builder().id(2L).name("Branch B").address("Address B").build();

        Mockito.when(branchService.getAll()).thenReturn(List.of(response1, response2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/branches"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /cigarette-distribution/v1/branches - Success")
    void createBranch() throws Exception {
        var request = BranchRequestBuilder.builder()
                .name("New Branch")
                .address("New Address")
                .build();

        var response = BranchResponseBuilder.builder()
                .id(10L)
                .name("New Branch")
                .address("New Address")
                .build();

        Mockito.when(branchService.save(Mockito.any(BranchRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/cigarette-distribution/v1/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /cigarette-distribution/v1/branches/{id} - Success")
    void updateBranch() throws Exception {
        var id = 5L;
        var request = BranchRequestBuilder.builder()
                .name("Updated Branch")
                .address("Updated Address")
                .build();

        var response = BranchResponseBuilder.builder()
                .id(id)
                .name("Updated Branch")
                .address("Updated Address")
                .build();

        Mockito.when(branchService.update(Mockito.eq(id), Mockito.any(BranchRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/cigarette-distribution/v1/branches/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE /cigarette-distribution/v1/branches/{id} - Success")
    void deleteBranch() throws Exception {
        var id = 7L;

        Mockito.doNothing().when(branchService).delete(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cigarette-distribution/v1/branches/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(branchService).delete(id);
    }
}
