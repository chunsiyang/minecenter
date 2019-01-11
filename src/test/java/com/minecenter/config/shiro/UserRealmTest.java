package com.minecenter.config.shiro;

import com.minecenter.config.shiro.jwt.JwtToken;
import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:config.properties")
public class UserRealmTest {

    private final String TEST_USER_ACCOUNT = "admin";

    private static String accessTokenExpireTime;
    private static String refreshTokenExpireTime;
    @Autowired
    private DefaultSecurityManager securityManager;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        UserRealmTest.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Value("${refreshTokenExpireTime}")
    public void setRefreshTokenExpireTime(String refreshTokenExpireTime) {
        UserRealmTest.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    /**
     * 清除缓存
     *
     * @author : chunsiyang
     * @date : 2019年01月10日 下午 07:50:22
     */
    @After
    public void clearCache() {
        redisUtil.removeAll();
    }


    /**
     * 当验证正确token时不抛出异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldHaveNoErrWhenGiveRightToken() {
        Long timeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign(TEST_USER_ACCOUNT, timeMillis, "userRealmTest");
        SecurityUtils.setSecurityManager(securityManager);
        redisUtil.set(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + TEST_USER_ACCOUNT,
                timeMillis, Integer.parseInt(refreshTokenExpireTime));
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JwtToken(token));
    }

    /**
     * 当验证超时token时抛出 AuthenticationException 异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:28:29
     */
    @Test
    public void shouldThrowAuthenticationExceptionWhenGiveTimeOutToken() {
        String token = JwtUtil.sign(TEST_USER_ACCOUNT,
                System.currentTimeMillis() - Long.valueOf(UserRealmTest.accessTokenExpireTime) * 1000 - 100,
                "userRealmTest");
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new JwtToken(token));
            Assert.fail();
        } catch (AuthenticationException exception) {
            // test pass
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
