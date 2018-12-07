package com.minecenter.util;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:config.properties")
public class JwtUtilTest {

    private static String accessTokenExpireTime;

    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        JwtUtilTest.accessTokenExpireTime = accessTokenExpireTime;
    }


    /**
     * 当执行sign应当返回token
     *
     * @author chunsiyang
     * @date 2018/12/05 10:57
     */
    @Test
    public void shouldReturnTokenWhenSign() {
        Long sysCurrentTimeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign("admin", sysCurrentTimeMillis, "JunitTest");
        Assert.assertTrue(!token.isEmpty());
    }


    /**
     * 当验证正确token时验证通过且不抛出异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:25:32
     */
    @Test
    public void shouldBeTruewhenVerifyRightToken() {
        Long sysCurrentTimeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign("admin", sysCurrentTimeMillis, "JunitTest");
        Assert.assertTrue(JwtUtil.verify(token));
    }


    /**
     * 当签名部分错误时抛出 SignatureVerificationException 异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:26:38
     */
    @Test
    public void shouldThrowSVEWhenGiveErrSignToken() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJhZG1pbiIsIm5iZiI6MTU0Mzk3MDUwNiwiaXNzIjoiSnVuaXRUZXN0IiwiZXhwIjoxNTQzOTcwODA2LCJpYXQiOjE1NDM5NzA1MDYsImp0aSI6ImE3NzQ0MWZmOWMxODQ0YzQ4Mjk3NThiYjFmNzRiMjRkIn0." +
                "LeHhoU26P_4d-UV1sqFqDCHEZ0CCCeQXE78buhewboo";
        try {
            JwtUtil.verify(token);
            Assert.fail();
        } catch (SignatureVerificationException e) {
            /* test pass */
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * 正确从token中获取数据
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:21
     */
    @Test
    public void shouldEqualsWhenClaim() {
        Long sysCurrentTimeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign("admin", sysCurrentTimeMillis, "JunitTest");
        Assert.assertEquals(JwtUtil.getSubject(token), "admin");
        Assert.assertEquals(JwtUtil.getIssuer(token), "JunitTest");
        Assert.assertEquals(JwtUtil.getIssuer(token), "JunitTest");
        Assert.assertEquals(JwtUtil.getExpiresAt(token).toString(), new Date(sysCurrentTimeMillis + Long.parseLong(accessTokenExpireTime) * 1000).toString());
        Assert.assertEquals(JwtUtil.getIssuedAt(token).toString(), new Date(sysCurrentTimeMillis).toString());
        Assert.assertEquals(JwtUtil.getNotBefore(token).toString(), new Date(sysCurrentTimeMillis).toString());
    }
}
