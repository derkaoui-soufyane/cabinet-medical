package com.example.cabinetmedical.DTO;

public class loginDTO {

    private String email;
    private String password;

    public loginDTO() {
    }

    public loginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}