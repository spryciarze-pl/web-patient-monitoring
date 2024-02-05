package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    Password findById(Integer id);
    Password findByHashedPassword(String password);
}
