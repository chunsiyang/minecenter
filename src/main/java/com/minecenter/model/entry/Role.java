package com.minecenter.model.entry;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "sys_role")
public class Role implements Serializable {
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
        this.name = name == null ? null : name.trim();
    }
}