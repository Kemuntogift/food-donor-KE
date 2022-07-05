
package com.pro.fooddonorke.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Charity {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("fooddonations")
    @Expose
    private List<String> fooddonations = null;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("descriptionImages")
    @Expose
    private List<String> descriptionImages = null;
    @SerializedName("contacts")
    @Expose
    private Contacts contacts;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Charity() {
    }

    /**
     * 
     * @param image
     * @param website
     * @param name
     * @param description
     * @param fooddonations
     * @param location
     * @param id
     * @param type
     * @param descriptionImages
     * @param contacts
     */
    public Charity(String image, String website, String name, String description, List<String> fooddonations, String location, Integer id, String type, List<String> descriptionImages, Contacts contacts) {
        super();
        this.image = image;
        this.website = website;
        this.name = name;
        this.description = description;
        this.fooddonations = fooddonations;
        this.location = location;
        this.id = id;
        this.type = type;
        this.descriptionImages = descriptionImages;
        this.contacts = contacts;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFooddonations() {
        return fooddonations;
    }

    public void setFooddonations(List<String> fooddonations) {
        this.fooddonations = fooddonations;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDescriptionImages() {
        return descriptionImages;
    }

    public void setDescriptionImages(List<String> descriptionImages) {
        this.descriptionImages = descriptionImages;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

}
