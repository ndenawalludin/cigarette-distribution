package id.co.surya.madistrindo.cigarette_distribution.repository;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
