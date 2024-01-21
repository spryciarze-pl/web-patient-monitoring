package se.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.db.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
