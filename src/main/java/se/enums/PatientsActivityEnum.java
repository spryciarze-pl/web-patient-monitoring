package se.enums;

public enum PatientsActivityEnum {
    REPORT_SYMPTOME("Report symptome"),
    MISCELLANEOUS("Request from patient");

    private String value;

    PatientsActivityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
