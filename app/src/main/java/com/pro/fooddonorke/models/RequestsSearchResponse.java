
package com.pro.fooddonorke.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RequestsSearchResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<DonationRequest> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RequestsSearchResponse() {
    }

    /**
     * 
     * @param data
     * @param message
     * @param status
     */
    public RequestsSearchResponse(Integer status, String message, List<DonationRequest> data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
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

    public List<DonationRequest> getData() {
        return data;
    }

    public void setData(List<DonationRequest> data) {
        this.data = data;
    }

}
