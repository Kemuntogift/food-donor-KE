package com.pro.fooddonorke.models;

public class ProfileData {
    String image;
    String name;
    String email;
    String phone;
    String location;
    String description;

    public ProfileData() {
    }

    public ProfileData(String image, String name, String email, String phone, String location, String description) {
        this.image = image;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
