
package com.pro.fooddonorke.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CharitiesSearchResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Charity> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CharitiesSearchResponse() {
    }

    /**
     * 
     * @param data
     * @param message
     * @param status
     */
    public CharitiesSearchResponse(Integer status, String message, List<Charity> data) {
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

    public List<Charity> getData() {
        return data;
    }

    public void setData(List<Charity> data) {
        this.data = data;
    }

}
