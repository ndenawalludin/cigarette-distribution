package id.co.surya.madistrindo.cigarette_distribution.controller;

import id.co.surya.madistrindo.cigarette_distribution.model.request.BranchRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.BranchResponse;
import id.co.surya.madistrindo.cigarette_distribution.service.BranchService;
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
@RequestMapping("/cigarette-distribution/v1/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public List<BranchResponse> getAll() {
        return branchService.getAll();
    }

    @PostMapping
    public BranchResponse create(@RequestBody BranchRequest req) {
        return branchService.save(req);
    }

    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable Long id, @RequestBody BranchRequest req) {
        return branchService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        branchService.delete(id);
    }
}
