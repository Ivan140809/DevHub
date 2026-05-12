package com.skillstack.devhub.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public class UserUpdateDTO {

    private String firstName;
    private String lastName;
    @Size(min = 4, max = 15, message = "El username debe tener entre 4 y 15 caracteres")
    private String username;
    private String phone;
    private List<String> preferences;

    public UserUpdateDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

}
