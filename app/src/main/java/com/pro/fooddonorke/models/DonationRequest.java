
package com.pro.fooddonorke.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DonationRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("charity_id")
    @Expose
    private Integer charityId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("location")
    @Expose
    private String location;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DonationRequest() {
    }

    /**
     * 
     * @param createdAt
     * @param charityId
     * @param location
     * @param id
     * @param message
     * @param updatedAt
     */
    public DonationRequest(Integer id, String message, Integer charityId, String createdAt, String updatedAt, String location) {
        super();
        this.id = id;
        this.message = message;
        this.charityId = charityId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCharityId() {
        return charityId;
    }

    public void setCharityId(Integer charityId) {
        this.charityId = charityId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
