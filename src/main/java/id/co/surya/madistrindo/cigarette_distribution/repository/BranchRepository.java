package id.co.surya.madistrindo.cigarette_distribution.repository;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
