package id.co.surya.madistrindo.cigarette_distribution.repository;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByProductId(Long productId);

    boolean existsByBranchFromId(Long branchId);

    boolean existsByBranchToId(Long branchId);

    List<Distribution> findAllByOrderByCreatedAtDesc();

}
