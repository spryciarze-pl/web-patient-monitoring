package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.db.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Integer id);

    User findByMail(String mail);

    List<User> findByConfirmedAndRoleId(boolean confirmed, int roleId);

    List<User> findByRoleId(Integer roleId);

    @Modifying
    @Transactional
    @Query("UPDATE User d SET d.confirmed = true WHERE d.id = :userId")
    void confirmUserById(@Param("userId") int userId);
}
