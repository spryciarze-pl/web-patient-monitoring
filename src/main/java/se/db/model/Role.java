package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "roles", schema = "public")
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

    // Constructors, getters, and setters

    public Role() {
        // Default constructor
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter and setter methods for all fields

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSamplePermission1() {
        return samplePermission1;
    }

    public void setSamplePermission1(boolean samplePermission1) {
        this.samplePermission1 = samplePermission1;
    }

    public boolean isSamplePermission2() {
        return samplePermission2;
    }

    public void setSamplePermission2(boolean samplePermission2) {
        this.samplePermission2 = samplePermission2;
    }

    public boolean isSamplePermission3() {
        return samplePermission3;
    }

    public void setSamplePermission3(boolean samplePermission3) {
        this.samplePermission3 = samplePermission3;
    }

    // toString, hashCode, equals, etc. methods can also be overridden if needed
}