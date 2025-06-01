package id.co.surya.madistrindo.cigarette_distribution.repository.user;

import id.co.surya.madistrindo.cigarette_distribution.model.entity.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
