package com.ujuzy.ujuzy.model;

public class Request {

    private String name;
    private String phone_number;
    private String date;
    private String time;
    private String service_id;
    private String request_info;


    public Request(String name, String phone_number, String date, String time, String service_id, String request_info) {
        this.name = name;
        this.phone_number = phone_number;
        this.date = date;
        this.time = time;
        this.service_id = service_id;
        this.request_info = request_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getRequest_info() {
        return request_info;
    }

    public void setRequest_info(String request_info) {
        this.request_info = request_info;
    }

}
