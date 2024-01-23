package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(int userId);
    List<Appointment> findByDoctorId(int userId);
}
