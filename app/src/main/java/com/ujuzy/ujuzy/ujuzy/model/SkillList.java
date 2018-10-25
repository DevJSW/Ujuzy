package com.ujuzy.ujuzy.ujuzy.model;

public class SkillList
{

    private String id;
    private String serviceName;
    private String cost;
    private String offerCost;
    private String billing;
    private Integer noOfPersonnel;

    public SkillList(String id, String serviceName, String cost, String offerCost, String billing, Integer noOfPersonnel) {
        this.id = id;
        this.serviceName = serviceName;
        this.cost = cost;
        this.offerCost = offerCost;
        this.billing = billing;
        this.noOfPersonnel = noOfPersonnel;
    }

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOfferCost() {
        return offerCost;
    }

    public void setOfferCost(String offerCost) {
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
