package se.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.db.model.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdAndDoctorDriven(int doctorId, boolean doctorDriven);

    List<Appointment> findByPatientIdAndDoctorDriven(int patientId, boolean doctorDriven);

    @Modifying
    @Transactional
    @Query("UPDATE Appointment d SET d.confirmed = true WHERE d.id = :appointmentId")
    void markAppointmentAsConfirmed(@Param("appointmentId") Integer appointmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Appointment d SET d.invalid = true, d.reason = :reason WHERE d.id = :appointmentId")
    void markAppointmentAsInvalid(@Param("appointmentId") Integer appointmentId, @Param("reason") String reason);
}
