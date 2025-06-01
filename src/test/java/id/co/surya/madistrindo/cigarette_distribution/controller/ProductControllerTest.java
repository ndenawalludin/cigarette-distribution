package id.co.surya.madistrindo.cigarette_distribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.surya.madistrindo.cigarette_distribution.model.request.ProductRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.request.ProductRequestBuilder;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.service.ProductService;
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
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /cigarette-distribution/v1/products - Success")
    void getAllProducts() throws Exception {
        var product1 = ProductResponseBuilder.builder()
                .id(1L)
                .name("Rokok A")
                .brand("Brand X")
                .category("Category 1")
                .build();

        var product2 = ProductResponseBuilder.builder()
                .id(2L)
                .name("Rokok B")
                .brand("Brand Y")
                .category("Category 2")
                .build();

        Mockito.when(productService.getAll()).thenReturn(List.of(product1, product2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cigarette-distribution/v1/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /cigarette-distribution/v1/products - Success")
    void createProduct() throws Exception {
        ProductRequest request = ProductRequestBuilder.builder()
                .name("Rokok C")
                .brand("Brand Z")
                .category("Category 3")
                .build();

        ProductResponse response = ProductResponseBuilder.builder()
                .id(3L)
                .name("Rokok C")
                .brand("Brand Z")
                .category("Category 3")
                .build();

        Mockito.when(productService.save(Mockito.any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/cigarette-distribution/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /cigarette-distribution/v1/products/{id} - Success")
    void updateProduct() throws Exception {
        Long id = 1L;
        ProductRequest request = ProductRequestBuilder.builder()
                .name("Rokok A Updated")
                .brand("Brand X Updated")
                .category("Category 1 Updated")
                .build();

        ProductResponse response = ProductResponseBuilder.builder()
                .id(id)
                .name("Rokok A Updated")
                .brand("Brand X Updated")
                .category("Category 1 Updated")
                .build();

        Mockito.when(productService.update(Mockito.eq(id), Mockito.any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/cigarette-distribution/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("DELETE /cigarette-distribution/v1/products/{id} - Success")
    void deleteProduct() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(productService).delete(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cigarette-distribution/v1/products/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(productService, Mockito.times(1)).delete(id);
    }
}
