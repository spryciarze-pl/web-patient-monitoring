package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.db.model.PatientsLimit;

@Repository
public interface PatientsLimitRepository extends JpaRepository<PatientsLimit, Integer> {
}
