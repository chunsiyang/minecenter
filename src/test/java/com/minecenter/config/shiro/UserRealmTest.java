package com.minecenter.config.shiro;

import com.minecenter.config.shiro.jwt.JwtToken;
import com.minecenter.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
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
    private static String accessTokenExpireTime;
    @Autowired
    private DefaultSecurityManager securityManager;

    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        UserRealmTest.accessTokenExpireTime = accessTokenExpireTime;
    }

    /**
     * 当验证正确token时不抛出异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldHaveNoErrWhenGiveRightToken() {
        String token = JwtUtil.sign("admin", System.currentTimeMillis(), "userRealmTest");
        SecurityUtils.setSecurityManager(securityManager);
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
        String token = JwtUtil.sign("admin",
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
