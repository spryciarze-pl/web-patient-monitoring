package se.views.appointments;

import java.time.LocalDateTime;

/* temporary */
public class AppointmentData
{
    private String name;
    private String surname;
    private String specialization; //we might want to have an ID for that so we can have specialziations stored in the db
    private LocalDateTime appointmentDate;

    public AppointmentData(String name, String surname, String specialization, LocalDateTime appointmentDate)
    {
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        this.appointmentDate = appointmentDate;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}