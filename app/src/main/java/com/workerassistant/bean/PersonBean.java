package com.workerassistant.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eva on 2017/4/21.
 */

public class PersonBean {
    String name;
    String age;
    String phone;
    String level;
    String city;
    String workType;
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getWorkType() {
        return workType;
    }

    public String getAge() {
        return age;
    }

    public String getLevel() {
        return level;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    @Override
    public String toString() {
        Map<String,String>map = new HashMap<>();
        map.put("name",getName());
        map.put("age",getAge());
        map.put("phone",getPhone());
        map.put("level",getLevel());
        map.put("city",getCity());
        map.put("workType",getWorkType());
        return map.toString();

    }
}

