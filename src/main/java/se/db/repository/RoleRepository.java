package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.db.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(Integer id);
}
