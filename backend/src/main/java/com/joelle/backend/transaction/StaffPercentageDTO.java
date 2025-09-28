package com.joelle.backend.transaction;

public class StaffPercentageDTO {
    private String staffName;
    private Double value;

    public StaffPercentageDTO(String staffName, Double value) {
        this.staffName = staffName;
        this.value = value;
    }

    public String getStaffName() { return staffName; }
    public Double getValue() { return value; }
}
