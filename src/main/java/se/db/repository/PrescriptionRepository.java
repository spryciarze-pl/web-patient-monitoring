package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
