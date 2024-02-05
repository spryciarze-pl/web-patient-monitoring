package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.db.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    Password findById(Integer id);
    Password findByHashedPassword(String password);
    @Modifying
    @Transactional
    @Query("UPDATE Password p SET p.hashedPassword = :newHashedPassword WHERE p.id = :passwordId")
    void updatePassword(@Param("passwordId") Integer passwordId, @Param("newHashedPassword") String newHashedPassword);
}
