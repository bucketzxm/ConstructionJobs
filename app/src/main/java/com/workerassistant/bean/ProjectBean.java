package com.workerassistant.bean;

/**
 * Created by eva on 2017/4/21.
 */

public class ProjectBean {
    String contactName;
    String contactPhone;
    String city;
    String workType;
    String startTime;
    String endTime;
    String numNeed;

    public String getCity() {
        return city;
    }

    public String getWorkType() {
        return workType;
    }

    public String getNumNeed() {
        return numNeed;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setNumNeed(String numNeed) {
        this.numNeed = numNeed;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
