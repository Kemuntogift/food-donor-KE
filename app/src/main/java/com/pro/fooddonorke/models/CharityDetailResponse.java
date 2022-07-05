
package com.pro.fooddonorke.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharityDetailResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Charity charity;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CharityDetailResponse() {
    }

    /**
     * 
     * @param charity
     * @param message
     * @param status
     */
    public CharityDetailResponse(Integer status, String message, Charity charity) {
        super();
        this.status = status;
        this.message = message;
        this.charity = charity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Charity getCharity() {
        return charity;
    }

    public void setCharity(Charity charity) {
        this.charity = charity;
    }

}
