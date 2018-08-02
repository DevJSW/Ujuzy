package com.ujuzy.ujuzy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkillList
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("cost")
    @Expose
    private Object cost;
    @SerializedName("offer_cost")
    @Expose
    private Object offerCost;
    @SerializedName("billing")
    @Expose
    private String billing;
    @SerializedName("no_of_personnel")
    @Expose
    private Integer noOfPersonnel;

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

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public Integer getNoOfPersonnel() {
        return noOfPersonnel;
    }

    public void setNoOfPersonnel(Integer noOfPersonnel) {
        this.noOfPersonnel = noOfPersonnel;
    }

}
