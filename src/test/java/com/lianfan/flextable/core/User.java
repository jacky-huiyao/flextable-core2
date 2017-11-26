package com.lianfan.flextable.core;

import java.util.Date;

public class User {
    private Integer code;
    private Integer age;
    private String name;
    private String address;
    private Company company;
    private Date birthday;

    public User(Integer code, Integer age, String name, String address,Date date) {
        this.code = code;
        this.age = age;
        this.name = name;
        this.address = address;
        this.birthday= date;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
