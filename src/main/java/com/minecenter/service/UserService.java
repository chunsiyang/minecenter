package com.minecenter.service;

import com.minecenter.annotation.CustomTransactional;
import com.minecenter.config.shiro.jwt.JwtToken;
import com.minecenter.exception.CustomException;
import com.minecenter.exception.CustomUnauthorizedException;
import com.minecenter.mapper.UserMapper;
import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.model.entry.User;
import com.minecenter.util.AesCipherUtil;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:config.properties")
public class UserService {

    private UserMapper userMapper;
    private RedisUtil redisUtil;

    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;
    /**
     * jwt签发人
     */
    @Value("${jwtIssuer}")
    private String jwtIssuer;

    /**
     * 密码最大长度
     * */
    @Value("${passwordMaxLen}")
    private Integer passwordMaxLen;

    @Autowired
    public UserService(UserMapper userMapper, RedisUtil redisUtil) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
    }

    /**
     * 登录授权
     *
     * @param  user      user
     * @return token
     * @author chunsiyang
     * @date 2018/12/22 16:21
     */
    public String login(User user) {
        // 查询数据库中的帐号信息
        User userTemp = new User();
        userTemp.setAccount(user.getAccount());
        userTemp = userMapper.selectOne(userTemp);
        if (userTemp == null) {
            throw new CustomUnauthorizedException("该帐号不存在(The account does not exist.)");
        }
        // 密码进行AES解密
        String key = AesCipherUtil.deCrypto(userTemp.getPassword());
        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        if (key.equals(user.getAccount() + user.getPassword())) {
            // 清除可能存在的Shiro权限信息缓存
            clearShiroCache(user.getAccount());
            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            Long currentTimeMillis = System.currentTimeMillis();
            redisUtil.set(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + user.getAccount(), currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JwtUtil.sign(user.getAccount(), currentTimeMillis, jwtIssuer);
            Subject subject = SecurityUtils.getSubject();
            subject.login(new JwtToken(token));
            return token;
        } else {
            throw new CustomUnauthorizedException("帐号或密码错误(Account or Password Error.)");
        }
    }

    /**
     * 更新用户信息
     *
     * @param  user      user
     * @author chunsiyang
     * @date 2018/12/22 16:21
     */
    @CustomTransactional(rollbackFor = Exception.class)
    public void update(User user) {
        // 查询数据库密码
        User userTemp = new User();
        userTemp.setAccount(user.getAccount());
        userTemp = userMapper.selectOne(userTemp);
        if(userTemp == null){
            throw new CustomUnauthorizedException("该帐号不存在(Account not exist.)");
        }else{
            user.setId(userTemp.getId());
        }
        String key = AesCipherUtil.enCrypto(user.getAccount() + user.getPassword());
        if(!userTemp.getPassword().equals(key)){
            // 密码以帐号+密码的形式进行AES加密
            if(user.getPassword().length() > passwordMaxLen){
                throw new CustomException("密码最多"+passwordMaxLen+"位(Password up to "+passwordMaxLen+" bits.)");
            }
            user.setPassword(key);
            userMapper.updateByPrimaryKeySelective(user);
        }else{
            throw new CustomException("更新失败(Update Failure)");
        }
    }

    /**
     * 用户主动注销
     *
     * @param  token     jwt token
     * @author chunsiyang
     * @date 2018/12/23 16:21
     */
    public void logout(String token) {
        String account = JwtUtil.getSubject(token);
        clearShiroCache(account);
        clearRefreshTokenInRedis(account);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    /**
     * 清除账户Shrio缓存
     *
     * @param account 需要清除的账户
     * @author : chunsiyang
     * @date : 2019年01月10日 下午 07:39:51
     */
    private void clearShiroCache(String account) {
        if (redisUtil.exists(RedisKeyEnum.PREFIX_SHIRO_CACHE_AUTHORIZATIONCACHE + account)) {
            redisUtil.del(RedisKeyEnum.PREFIX_SHIRO_CACHE_AUTHORIZATIONCACHE + account);
        }
        if (redisUtil.exists(RedisKeyEnum.PREFIX_SHIRO_CACHE_AUTHENTICATIONCACHE + account)) {
            redisUtil.del(RedisKeyEnum.PREFIX_SHIRO_CACHE_AUTHENTICATIONCACHE + account);
        }
    }

    /**
     * 清除账户RefreshToken
     * @param account 需要清除的账户
     * @author : chunsiyang
     * @date : 2019年01月10日 下午 07:39:51
     */
    private void clearRefreshTokenInRedis(String account) {
        if (redisUtil.exists(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            redisUtil.del(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + account);
        }
    }

}
