package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.PatientsActivity;

public interface PatientsActivityRepository extends JpaRepository<PatientsActivity, Long> {

    PatientsActivity findByDoctorsRequestId(int activityId);

    @Transactional
    void deleteByPatientId(Integer id);

    @Transactional
    void deleteByDoctorsRequestId(Integer id);

}
