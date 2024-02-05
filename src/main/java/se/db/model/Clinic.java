package se.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "clinics", schema = "public")
@Data
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    @Size(max = 32)
    private String phone;

    @NotBlank
    @Size(max = 64)
    private String mail;

    public Clinic() {
    }

    public Clinic(String name, String address, String phone, String mail) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
    }
}