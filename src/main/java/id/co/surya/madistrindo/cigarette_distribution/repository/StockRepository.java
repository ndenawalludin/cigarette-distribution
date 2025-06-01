package id.co.surya.madistrindo.cigarette_distribution.repository;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import id.co.surya.madistrindo.cigarette_distribution.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT * FROM stocks ORDER BY updated_at DESC NULLS LAST", nativeQuery = true)
    List<Stock> findAllOrderByUpdatedAtDesc();

    List<Stock> findByBranchId(Long branchId);

    Optional<Stock> findByBranchIdAndProductId(Long branchId, Long productId);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s")
    int sumAllQuantity();

    void deleteAllByProduct(Product product);

    void deleteAllByBranch(Branch product);

}
