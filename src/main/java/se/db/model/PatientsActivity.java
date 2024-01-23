package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "patients_activity", schema = "public")
@Data
public class PatientsActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime time;

    @Column(name = "doctors_request_id", nullable = false)
    private Integer doctorsRequestId;

    @Column(name = "result", length = 256, nullable = false)
    private String result;

    @Column(name = "type", length = 64, nullable = false)
    private String type;

    public PatientsActivity() {
    }
}