package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.PatientsActivity;

public interface PatientsActivityRepository extends JpaRepository<PatientsActivity, Long> {
}
