package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions", schema = "public")
@Data
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "doctor_id")
    private Integer doctorId;

    @NotNull
    @Column(name = "patient_id")
    private Integer patientId;

    @NotBlank
    @Column(name = "medicine", length = 32)
    private String medicine;

    @NotNull
    @Column(name = "prescription_time", columnDefinition = "timestamp without time zone")
    private LocalDateTime prescriptionTime;

    public Prescription() {
    }

    public Prescription(Integer doctorId, Integer patientId, String medicine) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.medicine = medicine;
        this.prescriptionTime = LocalDateTime.now();
    }

}