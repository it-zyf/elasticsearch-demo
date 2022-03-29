package com.example.demo.controller;

public class APIResponse {

    public static final Integer OK = 200;
    public static final Integer ERROR = 500;
    private Integer responseCode;
    private String reasonCode = "";
    private Object data;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
