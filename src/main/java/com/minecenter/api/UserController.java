package com.minecenter.api;

import com.minecenter.exception.CustomUnauthorizedException;
import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.model.common.ResponseBean;
import com.minecenter.model.entry.User;
import com.minecenter.service.UserService;
import com.minecenter.util.AesCipherUtil;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * UserController
 *
 * @author Wang926454
 * @date 2018/8/29 15:45
 */
@RestController
@RequestMapping("/user")
@PropertySource("classpath:config.properties")
public class UserController {

    @Autowired
    UserService userService;
    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;

    /**
     * 登录授权
     *
     * @param user                user
     * @param httpServletResponse httpServletResponse
     * @return ResponseBean
     * @author yangchunsi
     * @date 2018/8/30 16:21
     */
    @PostMapping("/login")
    public ResponseBean login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        // 查询数据库中的帐号信息
        User userTemp = new User();
        userTemp.setAccount(user.getAccount());
        userTemp = userService.selectOne(userTemp);
        if (userTemp == null) {
            throw new CustomUnauthorizedException("该帐号不存在(The account does not exist.)");
        }
        // 密码进行AES解密
        String key = AesCipherUtil.deCrypto(userTemp.getPassword());
        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        if (key.equals(user.getAccount() + user.getPassword())) {
            // 清除可能存在的Shiro权限信息缓存
            if(RedisUtil.exists(RedisKeyEnum.PREFIX_SHIRO_CACHE + user.getAccount())){
                RedisUtil.del(RedisKeyEnum.PREFIX_SHIRO_CACHE + user.getAccount());
            }
            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            Long currentTimeMillis = System.currentTimeMillis();
            RedisUtil.set(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + user.getAccount(), currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));

            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JwtUtil.sign(user.getAccount(), currentTimeMillis, "userController");
            httpServletResponse.setHeader("Authorization", token);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return new ResponseBean(HttpStatus.OK.value(), "登录成功(Login Success.)", null);
        } else {
            throw new CustomUnauthorizedException("帐号或密码错误(Account or Password Error.)");
        }
    }

}