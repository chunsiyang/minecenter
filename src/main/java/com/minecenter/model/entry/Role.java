package com.minecenter.model.entry;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "sys_role")
public class Role extends BaseBean implements Serializable {
    /**
     * ID
     */
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;

    /**
     * 角色名称
     */
    private String name;


    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

}