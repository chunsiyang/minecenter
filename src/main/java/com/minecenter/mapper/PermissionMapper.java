package com.minecenter.mapper;

import com.minecenter.model.entry.Permission;
import com.minecenter.model.entry.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PermissionMapper extends Mapper<Permission> {

    /**
     * 根据Role查询Permission
     *
     * @param role 查询条件
     * @return java.util.List<com.wang.model.PermissionDto>
     * @author chunsiyang
     * @date 2018/8/31 11:30
     */
    List<Permission> findPermissionByRole(Role role);
}