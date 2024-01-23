package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByMail(String mail);
}
