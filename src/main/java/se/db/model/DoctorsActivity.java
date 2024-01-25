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
@Table(name = "doctors_activity", schema = "public")
@Data
public class DoctorsActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "description", length = 128)
    private String description;

    @Column(name = "activity_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime activityTime;

    @Column(name = "deadline_time", nullable = false)
    private LocalDateTime deadlineTime;
    public DoctorsActivity() {
    }

    public DoctorsActivity(Integer doctorId, Integer patientId, String type, String description, LocalDateTime activityTime, LocalDateTime deadlineTime) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.type = type;
        this.description = description;
        this.activityTime = activityTime;
        this.deadlineTime = deadlineTime;
    }


}