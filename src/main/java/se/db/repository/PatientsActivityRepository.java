package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.db.model.PatientsActivity;

import java.util.List;

@Repository
public interface PatientsActivityRepository extends JpaRepository<PatientsActivity, Long> {

    PatientsActivity findByDoctorsRequestId(int activityId);

    List<PatientsActivity> findByPatientId(int patientId);
    @Transactional
    void deleteByPatientId(Integer id);

    @Transactional
    void deleteByDoctorsRequestId(Integer id);

}
