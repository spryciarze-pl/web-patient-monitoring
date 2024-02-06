package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "public")
@Data
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "name", length = 32)
    private String name;

    @NotBlank
    @Column(name = "surname", length = 32)
    private String surname;

    @NotBlank
    @Column(name = "pin", length = 11)
    private String pin;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @NotBlank
    @Column(name = "mail")
    private String mail;

    @NotNull
    @Column(name = "registration_time", columnDefinition = "timestamp without time zone")
    private LocalDateTime registrationTime;

    @NotNull
    @Column(name = "confirmed")
    private boolean confirmed;

    @NotNull
    @Column(name = "password_id")
    private Integer passwordId;

    @NotNull
    @Column(name = "role_id")
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    public User() {
    }

    public User(String name, String surname, String pin, String phone, String mail,
                Integer passwordId, Integer roleId, Clinic clinic, Specialization specialization) {
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.phone = phone;
        this.mail = mail;
        this.registrationTime = LocalDateTime.now();
        this.confirmed = false;
        this.passwordId = passwordId;
        this.roleId = roleId;
        this.clinic = clinic;
        this.specialization = specialization;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}