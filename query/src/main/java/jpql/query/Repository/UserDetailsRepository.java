package jpql.query.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jpql.query.Entity.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    List<UserDetails> findUserDetailsByName(String name, Pageable page);

    List<UserDetails> findUserDetailsByNameAndPhone(String name, String phone);

    List<UserDetails> findUserDetailsByNameOrPhone(String name, String phone);

    List<UserDetails> findUserDetailsByNameLike(String name);

    List<UserDetails> findUserDetailsByNameContaining(String name, Pageable page);

    // Using JQPL QUery for raw queries
    @Query("SELECT u FROM UserDetails u WHERE u.name = :userName")
    List<UserDetails> findUserByName(@Param("userName") String name);

    // Using left join in query
    @Query("SELECT u FROM UserDetails u JOIN u.userAddress ad")
    List<UserDetails> findUserDetailsWithUserAddress();

    void deleteByUserId(Long id);
}
