package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.DoctorsActivity;

public interface DoctorsActivityRepository extends JpaRepository<DoctorsActivity, Long> {
}
