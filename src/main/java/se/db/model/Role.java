package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "roles", schema = "public")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sample_permission_1")
    private boolean samplePermission1;

    @Column(name = "sample_permission_2")
    private boolean samplePermission2;

    @Column(name = "sample_permission_3")
    private boolean samplePermission3;

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}