package role_base.authorization.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import role_base.authorization.Entity.UserRegisterEntity;



@Repository
public interface UserRegisterEntityRepository extends JpaRepository<UserRegisterEntity, Long> {
    Optional<UserRegisterEntity> findByUsername(String username);
}
