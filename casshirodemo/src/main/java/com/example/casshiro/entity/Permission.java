package com.example.casshiro.entity;

import lombok.ToString;

@ToString
public class Permission implements org.apache.shiro.authz.Permission {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean implies(org.apache.shiro.authz.Permission permission) {
        return false;
    }
}
