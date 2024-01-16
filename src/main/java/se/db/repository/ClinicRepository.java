package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
