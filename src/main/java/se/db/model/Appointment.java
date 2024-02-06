package se.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "appointment_type", nullable = false)
    private String type;

    @Column(name = "description", length = 128)
    private String description;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "location", length = 64, nullable = false)
    private String location;

    @Column(name = "appointment_end")
    private LocalDateTime appointmentEnd;

    @Column(name = "confirmed", nullable = false)
    private boolean confirmed = false;

    @Column(name = "invalid", nullable = false)
    private boolean invalid = false;

    @Column(name = "reason", length = 64)
    private String reason;

    @Column(name = "doctor_driven", nullable = false)
    private boolean doctorDriven = true;

}