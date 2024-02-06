package se.enums;

public enum AppointmentType {

    STATIONARY("stationary"),
    ONLINE("online");

    private String value;

    AppointmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
