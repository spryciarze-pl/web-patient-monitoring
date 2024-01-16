package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(Integer id);
}
