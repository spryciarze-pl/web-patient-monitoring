package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.db.model.PatientsLimit;

@Repository
public interface PatientsLimitRepository extends JpaRepository<PatientsLimit, Long> {

    PatientsLimit findByDoctorId(int doctorId);

    @Modifying
    @Transactional
    @Query("UPDATE PatientsLimit d SET d.pLimit = :pLimit1 WHERE d.doctorId = :doctorId1")
    void updateDoctorsPatientLimit(@Param("pLimit1") int pLimit, @Param("doctorId1") int doctorId);
}
