
package com.pro.fooddonorke.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Contacts {

    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("email")
    @Expose
    private String email;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Contacts() {
    }

    /**
     * 
     * @param twitter
     * @param phone
     * @param facebook
     * @param instagram
     * @param email
     */
    public Contacts(String twitter, String phone, String facebook, String instagram, String email) {
        super();
        this.twitter = twitter;
        this.phone = phone;
        this.facebook = facebook;
        this.instagram = instagram;
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
