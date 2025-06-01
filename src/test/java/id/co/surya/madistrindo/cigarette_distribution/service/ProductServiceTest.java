package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import id.co.surya.madistrindo.cigarette_distribution.model.request.ProductRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponse;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    StockRepository stockRepository;

    Product product;
    ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Product A", "Category A", "Brand A");
        productRequest = new ProductRequest("Product A", "Category A", "Brand A");
    }

    @Test
    @DisplayName("Get all products - should return list of ProductResponse")
    void getAllProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> result = productService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Product A", result.get(0).name());
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Save product - should save product and initialize stock")
    void saveProductSuccess() {
        Branch branch = new Branch(1L, "Branch A", "Jl. Mawar", "08123456789", "Region A");

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        Mockito.when(branchRepository.findAll()).thenReturn(List.of(branch));
        Mockito.when(stockRepository.saveAll(Mockito.anyList())).thenReturn(List.of());

        ProductResponse result = productService.save(productRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Product A", result.name());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
        Mockito.verify(stockRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    @DisplayName("Save product - should throw NoSuchElementException if no branch found")
    void saveProductNoBranch() {
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        Mockito.when(branchRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(NoSuchElementException.class, () -> productService.save(productRequest));

        Mockito.verify(stockRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    @Test
    @DisplayName("Update product - should return updated ProductResponse")
    void updateProductSuccess() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ProductResponse result = productService.update(1L, productRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Product A", result.name());
        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    @DisplayName("Update product - should throw NoSuchElementException when not found")
    void updateProductNotFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> productService.update(1L, productRequest));

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Delete product - should delete stock if product exists")
    void deleteProductExists() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        Mockito.verify(stockRepository, Mockito.times(1)).deleteAllByProduct(product);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete product - should only call deleteById if product not found")
    void deleteProductNotFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        productService.delete(1L);

        Mockito.verify(stockRepository, Mockito.never()).deleteAllByProduct(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }
}
