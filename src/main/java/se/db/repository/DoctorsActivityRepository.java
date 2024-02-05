package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.db.model.DoctorsActivity;

import java.util.List;

public interface DoctorsActivityRepository extends JpaRepository<DoctorsActivity, Long> {

    DoctorsActivity findById(Integer id);

    List<DoctorsActivity> findByDoctorId(Integer id);

    List<DoctorsActivity> findByPatientId(Integer id);

    @Transactional
    void deleteByPatientId(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE DoctorsActivity d SET d.completed = true WHERE d.id = :activityId")
    void markDoctorsActivityAsComplete(@Param("activityId") Integer activityId);

    @Query("SELECT da.patientId FROM DoctorsActivity da WHERE da.doctorId = :doctorId")
    List<Integer> findPatientIdsByDoctorId(@Param("doctorId") Integer doctorId);

}
