package se.db.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients_limit", schema = "public")
@Data
@NoArgsConstructor
public class PatientsLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;

    @Column(name = "p_limit")
    private Integer pLimit;

    public PatientsLimit(Integer doctorId, Integer pLimit) {
        this.doctorId = doctorId;
        this.pLimit = pLimit;
    }
}
