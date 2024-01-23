package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
