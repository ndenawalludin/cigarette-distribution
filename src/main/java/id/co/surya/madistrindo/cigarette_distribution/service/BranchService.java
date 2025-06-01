package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.request.BranchRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.BranchResponse;
import id.co.surya.madistrindo.cigarette_distribution.model.response.BranchResponseBuilder;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
import id.co.surya.madistrindo.cigarette_distribution.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final StockRepository stockRepository;

    public List<BranchResponse> getAll() {
        return branchRepository.findAll().stream()
                .map(dataBranch -> BranchResponseBuilder.builder()
                        .id(dataBranch.getId())
                        .name(dataBranch.getName())
                        .address(dataBranch.getAddress())
                        .contact(dataBranch.getContact())
                        .region(dataBranch.getRegion())
                        .build())
                .toList();
    }

    public BranchResponse save(BranchRequest req) {
        var branch = new Branch(null, req.name(), req.address(), req.contact(), req.region());
        var saved = branchRepository.save(branch);
        return BranchResponseBuilder.builder()
                .id(saved.getId())
                .name(saved.getName())
                .address(saved.getAddress())
                .contact(saved.getContact())
                .region(saved.getRegion())
                .build();
    }

    public BranchResponse update(Long id, BranchRequest req) {
        var branch = branchRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Branch not found"));
        branch.setName(req.name());
        branch.setAddress(req.address());
        branch.setContact(req.contact());
        branch.setRegion(req.region());
        var updated = branchRepository.save(branch);
        return BranchResponseBuilder.builder()
                .id(updated.getId())
                .name(updated.getName())
                .address(updated.getAddress())
                .contact(updated.getContact())
                .region(updated.getRegion())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        branchRepository.findById(id).ifPresent(stockRepository::deleteAllByBranch);
        branchRepository.deleteById(id);
    }
}
