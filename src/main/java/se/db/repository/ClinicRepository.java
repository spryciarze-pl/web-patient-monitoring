package se.db.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findById(Integer clinicId);
}
