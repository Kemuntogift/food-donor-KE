package com.pro.fooddonorke.models;

public class ProfileData {
    String phone;
    String location;
    String description;

    public ProfileData() {
    }

    public ProfileData(String phone, String location, String description) {
        this.phone = phone;
        this.location = location;
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
