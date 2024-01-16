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
import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "public")
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

    // Constructors, getters, and setters

    public User() {
        // Default constructor
    }

    // Constructor without IDs for creating new users
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(Integer passwordId) {
        this.passwordId = passwordId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}