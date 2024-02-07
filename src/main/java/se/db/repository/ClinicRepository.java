package se.db.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.db.model.Clinic;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findById(Integer clinicId);
}
