package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.db.model.Assignment;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByDoctorId(Integer id);

    Assignment findByPatientId(Integer id);

    @Transactional
    void deleteByPatientId(Integer id);

    @Query("SELECT a.doctorId FROM Assignment a GROUP BY a.doctorId HAVING COUNT(a.doctorId) > 5")
    List<Integer> findDoctorsWithMoreThan5Assignments();
}
