package com.minecenter.mapper;

import com.minecenter.model.entry.Role;
import com.minecenter.model.entry.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据User查询Role
     *
     * @param user 封装查询条件
     * @return java.util.List<com.wang.model.RoleDto>
     * @author chunsiyang
     * @date 2018/8/31 11:30
     */
    List<Role> findRoleByUser(User user);
}