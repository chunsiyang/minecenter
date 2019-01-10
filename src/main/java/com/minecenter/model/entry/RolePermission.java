package com.minecenter.model.entry;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_role_permission")
public class RolePermission extends BaseBean implements Serializable {
    /**
     * ID
     */
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private String permissionId;

    /**
     * 创建人
     */
    @Column(name = "creat_id")
    private String creatId;

    /**
     * 创建时间
     */
    @Column(name = "creat_date")
    private Date creatDate;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 数据状态(0:删除，1:正常)
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

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
     * 获取角色id
     *
     * @return role_id - 角色id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 获取权限id
     *
     * @return permission_id - 权限id
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * 设置权限id
     *
     * @param permissionId 权限id
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }
}