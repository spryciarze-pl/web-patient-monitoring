package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.db.model.Password;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    Password findById(Integer id);

    Password findByHashedPassword(String password);
}
