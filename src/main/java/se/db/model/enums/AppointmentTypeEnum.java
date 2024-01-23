package se.db.model.enums;

public enum AppointmentTypeEnum {

    STATIONARY("stationary"),
    ONLINE("online");

    String valueAsString;

    AppointmentTypeEnum(String valueAsString) {
        this.valueAsString = valueAsString;
    }
}
