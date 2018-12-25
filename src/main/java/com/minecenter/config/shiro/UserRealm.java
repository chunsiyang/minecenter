package com.minecenter.config.shiro;


import com.minecenter.config.shiro.jwt.JwtToken;
import com.minecenter.mapper.PermissionMapper;
import com.minecenter.mapper.RoleMapper;
import com.minecenter.mapper.UserMapper;
import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.model.entry.Permission;
import com.minecenter.model.entry.Role;
import com.minecenter.model.entry.User;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * 自定义Realm
 *
 * @author chunsiyang
 * @date 2018/12/21 14:10
 */
@Service
public class UserRealm extends AuthorizingRealm {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public UserRealm(UserMapper userMapper, RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String account = JwtUtil.getSubject(principals.toString());
        User user = new User();
        user.setAccount(account);
        // 查询用户角色
        List<Role> roleList = roleMapper.findRoleByUser(user);
        for (Role role : roleList) {
            // 添加角色
            simpleAuthorizationInfo.addRole(role.getName());
            // 根据用户角色查询权限
            List<Permission> permissionList = permissionMapper.findPermissionByRole(role);
            for (Permission permission : permissionList) {
                // 添加权限
                simpleAuthorizationInfo.addStringPermission(permission.getPerCode());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getSubject(token);
        // 帐号为空
        if (StringUtil.isEmpty(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
        // 查询用户是否存在
        User user = new User();
        user.setAccount(account);
        user = userMapper.selectOne(user);
        if (user == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if(JwtUtil.verify(token) && RedisUtil.exists(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + account)){
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = RedisUtil.get(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (new Date(Long.valueOf(currentTimeMillisRedis)).toString().equals(JwtUtil.getIssuedAt(token).toString())) {
                return new SimpleAuthenticationInfo(token, token, "userRealm");
            }
        }
        if (JwtUtil.getExpiresAt(token).getTime() > System.currentTimeMillis()) {
             return new SimpleAuthenticationInfo(token, token, "userRealm");
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }
}
