package com.ujuzy.ujuzy.ujuzy.model;

import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shephard on 7/3/2018.
 */

public class Datum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_details")
    @Expose
    private String serviceDetails;
    @SerializedName("cost")
    @Expose
    private Object cost;
    @SerializedName("offer_cost")
    @Expose
    private Object offerCost;
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("travel")
    @Expose
    private Boolean travel;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("duration")
    @Expose
    private Duration duration;
    @SerializedName("no_of_personnel")
    @Expose
    private Integer noOfPersonnel;
    @SerializedName("published")
    @Expose
    private Boolean published;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_new")
    @Expose
    private Boolean isNew;
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("updated_by")
    @Expose
    private Object updatedBy;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("skill_list")
    @Expose
    private List<SkillList> skillList = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public Object getCost() {
        return cost;
    }

    public void setCost(Object cost) {
        this.cost = cost;
    }

    public Object getOfferCost() {
        return offerCost;
    }

    public void setOfferCost(Object offerCost) {
        this.offerCost = offerCost;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getTravel() {
        return travel;
    }

    public void setTravel(Boolean travel) {
        this.travel = travel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Integer getNoOfPersonnel() {
        return noOfPersonnel;
    }

    public void setNoOfPersonnel(Integer noOfPersonnel) {
        this.noOfPersonnel = noOfPersonnel;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SkillList> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<SkillList> skillList) {
        this.skillList = skillList;
    }



}
