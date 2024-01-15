package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

}