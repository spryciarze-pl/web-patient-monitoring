package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.db.model.DoctorsActivity;

import java.util.List;

public interface DoctorsActivityRepository extends JpaRepository<DoctorsActivity, Long> {

    List<DoctorsActivity> findByDoctorId(Integer id);

    List<DoctorsActivity> findByPatientId(Integer id);

    @Query("SELECT da.patientId FROM DoctorsActivity da WHERE da.doctorId = :doctorId")
    List<Integer> findPatientIdsByDoctorId(@Param("doctorId") Integer doctorId);

}
