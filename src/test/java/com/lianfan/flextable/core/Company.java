package com.lianfan.flextable.core;

import java.util.Date;

public class Company {
    private String name;
    private String address;
    private Integer employees;
    private Date createYear;

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

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public Date getCreateYear() {
        return createYear;
    }

    public void setCreateYear(Date createYear) {
        this.createYear = createYear;
    }
}
