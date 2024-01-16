package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions", schema = "public")
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

    // Constructors, getters, and setters

    public Prescription() {
        // Default constructor
    }

    public Prescription(Integer doctorId, Integer patientId, String medicine) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.medicine = medicine;
        this.prescriptionTime = LocalDateTime.now();
    }

    // Getter and setter methods for all fields

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public LocalDateTime getPrescriptionTime() {
        return prescriptionTime;
    }

    public void setPrescriptionTime(LocalDateTime prescriptionTime) {
        this.prescriptionTime = prescriptionTime;
    }

    // toString, hashCode, equals, etc. methods can also be overridden if needed
}