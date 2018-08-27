package com.ujuzy.ujuzy.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmJobService extends RealmObject
{
    @PrimaryKey
    private String id;
    private String serviceName;
    private String serviceDetails;
    private String cost;
    private String category;
    private Boolean travel;
    private String image;
    private String additional_info;
    private String created_by;
    private String user_role;
    private String first_name;
    private String last_name;
    private String service_duration_days;
    private String service_duration_hours;
    private String user_id;
    private double latitude;
    private double longitude;
    private String city;
    private String user_thumb;
    private int no_of_personnel;
    private String created_at;

    public RealmJobService(RealmList<RealmSkillList> skillList) {
        this.skillList = skillList;
    }

    private RealmList<RealmSkillList> skillList = null;


    public RealmJobService(String id, String serviceName, String serviceDetails, String cost, String category, Boolean travel, String image, String additional_info, String created_by, String user_role, String first_name, String last_name, String service_duration_days, String service_duration_hours, String user_id, double latitude, double longitude, String city, String user_thumb, int no_of_personnel, String createdBy, String created_at, RealmList<RealmSkillList> skillList) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceDetails = serviceDetails;
        this.cost = cost;
        this.category = category;
        this.travel = travel;
        this.image = image;
        this.additional_info = additional_info;
        this.created_by = created_by;
        this.user_role = user_role;
        this.first_name = first_name;
        this.last_name = last_name;
        this.service_duration_days = service_duration_days;
        this.service_duration_hours = service_duration_hours;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.user_thumb = user_thumb;
        this.no_of_personnel = no_of_personnel;
        this.createdBy = createdBy;
        this.created_at = created_at;
        this.skillList = skillList;
    }


    public RealmJobService() {

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private String createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getService_duration_days() {
        return service_duration_days;
    }

    public void setService_duration_days(String service_duration_days) {
        this.service_duration_days = service_duration_days;
    }

    public String getService_duration_hours() {
        return service_duration_hours;
    }

    public void setService_duration_hours(String service_duration_hours) {
        this.service_duration_hours = service_duration_hours;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_thumb() {
        return user_thumb;
    }

    public void setUser_thumb(String user_thumb) {
        this.user_thumb = user_thumb;
    }

    public int getNo_of_personnel() {
        return no_of_personnel;
    }

    public void setNo_of_personnel(int no_of_personnel) {
        this.no_of_personnel = no_of_personnel;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public RealmList<RealmSkillList> getSkillList() {
        return skillList;
    }

    public void setSkillList(RealmList<RealmSkillList> skillList) {
        this.skillList = skillList;
    }
}
