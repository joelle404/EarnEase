package com.joelle.backend.security;

import com.joelle.backend.staff.Staff;

public class LoginResponse {
    private String token;
    private Staff staff;

    public LoginResponse(String token, Staff staff) {
        this.token = token;
        this.staff = staff;
    }

    public String getToken() {
        return token;
    }

    public Staff getStaff() {
        return staff;
    }
}
