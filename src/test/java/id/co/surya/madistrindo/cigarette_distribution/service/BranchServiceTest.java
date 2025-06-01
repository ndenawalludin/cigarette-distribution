package id.co.surya.madistrindo.cigarette_distribution.service;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.request.BranchRequest;
import id.co.surya.madistrindo.cigarette_distribution.model.response.BranchResponse;
import id.co.surya.madistrindo.cigarette_distribution.repository.BranchRepository;
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
class BranchServiceTest {

    @InjectMocks
    BranchService branchService;

    @Mock
    BranchRepository branchRepository;

    @Mock
    StockRepository stockRepository;

    Branch branch;
    BranchRequest branchRequest;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "Branch A", "Jl. Mawar", "08123456789", "Region A");
        branchRequest = new BranchRequest("Branch A", "Jl. Mawar", "08123456789", "Region A");
    }

    @Test
    @DisplayName("Get all branches - should return list of BranchResponse")
    void getAllBranches() {
        Mockito.when(branchRepository.findAll()).thenReturn(List.of(branch));

        List<BranchResponse> result = branchService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Branch A", result.get(0).name());
        Mockito.verify(branchRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Save branch - should return saved BranchResponse")
    void saveBranch() {
        Mockito.when(branchRepository.save(Mockito.any(Branch.class))).thenReturn(branch);

        BranchResponse result = branchService.save(branchRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Branch A", result.name());
        Mockito.verify(branchRepository, Mockito.times(1)).save(Mockito.any(Branch.class));
    }

    @Test
    @DisplayName("Update branch - should return updated BranchResponse")
    void updateBranchSuccess() {
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        Mockito.when(branchRepository.save(Mockito.any(Branch.class))).thenReturn(branch);

        BranchResponse result = branchService.update(1L, branchRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Branch A", result.name());
        Mockito.verify(branchRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(branchRepository, Mockito.times(1)).save(branch);
    }

    @Test
    @DisplayName("Update branch - should throw NoSuchElementException when not found")
    void updateBranchNotFound() {
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> branchService.update(1L, branchRequest));

        Mockito.verify(branchRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(branchRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("Delete branch - should call delete methods")
    void deleteBranch() {
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        branchService.delete(1L);

        Mockito.verify(stockRepository, Mockito.times(1)).deleteAllByBranch(branch);
        Mockito.verify(branchRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete branch - should only deleteById when not found")
    void deleteBranchNotFound() {
        Mockito.when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        branchService.delete(1L);

        Mockito.verify(stockRepository, Mockito.never()).deleteAllByBranch(Mockito.any());
        Mockito.verify(branchRepository, Mockito.times(1)).deleteById(1L);
    }
}
