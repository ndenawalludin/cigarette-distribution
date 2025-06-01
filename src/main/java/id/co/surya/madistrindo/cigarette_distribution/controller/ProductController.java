package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.model.request.ProductRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.ProductResponse;
import id.co.surya.madistrindo.cigarette_distribution.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cigarette-distribution/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest req) {
        return productService.save(req);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
