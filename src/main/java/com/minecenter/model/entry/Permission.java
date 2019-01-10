package com.minecenter.model.entry;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "sys_permission")
public class Permission extends BaseBean implements Serializable {
    /**
     * ID
     */
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 权限代码字符串
     */
    @Column(name = "per_code")
    private String perCode;

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
     * 获取资源名称
     *
     * @return name - 资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源名称
     *
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取权限代码字符串
     *
     * @return per_code - 权限代码字符串
     */
    public String getPerCode() {
        return perCode;
    }

    /**
     * 设置权限代码字符串
     *
     * @param perCode 权限代码字符串
     */
    public void setPerCode(String perCode) {
        this.perCode = perCode == null ? null : perCode.trim();
    }

}