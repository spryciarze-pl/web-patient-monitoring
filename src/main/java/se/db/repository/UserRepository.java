package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.db.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Integer id);
    User findByMail(String mail);
    List<User> findByRoleId(Integer roleId);
}
