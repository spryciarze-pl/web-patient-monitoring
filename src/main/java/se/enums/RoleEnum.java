package se.enums;

public enum RoleEnum {
    PATIENT("Patient", 3),
    DOCTOR("Doctor", 2);

    private String description;
    private Integer roleDbVal;

    RoleEnum(String description, Integer roleDbVal) {
        this.description = description;
        this.roleDbVal = roleDbVal;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRoleDbVal() {
        return roleDbVal;
    }
}
