package se.enums;

public enum DoctorsActivityEnum {

    TEMPERATURE("Take temperature"),
    PRESSURE("Measure Pressure"),
    SUGAR("Measure Sugar Level"),
    OXYGEN("Measure oxygen-blood saturation"),
    PRESCRIPTION("Issue Prescription"),
    MEDICINE("Take medicine"),
    MISCELLANEOUS("Request from doctor");

    private String value;

    DoctorsActivityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
