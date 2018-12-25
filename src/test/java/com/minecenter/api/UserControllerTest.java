package com.minecenter.api;

import com.minecenter.config.shiro.jwt.JwtToken;
import com.minecenter.exception.CustomUnauthorizedException;
import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@PropertySource("classpath:config.properties")
public class UserControllerTest {

    @Autowired
    private DefaultSecurityManager securityManager;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Value("${accessTokenExpireTime}")
    private  String accessTokenExpireTime;

    @Before
    public void setUp() {

        mvc = MockMvcBuilders.webAppContextSetup(context).build();  //构造MockMvc
        SecurityUtils.setSecurityManager(securityManager);
    }

    /**
     * 当提供正确用户名密码时返回200状态
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldReturn200GiveLoginWithRightNamePasswd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"account\":\"admin\",\"password\":\"admin\"}")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().isOk());
    }

    /**
     * 当提供正确用户名错误密码时抛出
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldThrowNSEGiveLoginWithRightNameErrPasswd() throws Exception {
        try {
            mvc.perform(MockMvcRequestBuilders.post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"account\":\"admin\",\"password\":\"123\"}")
                    .accept(MediaType.APPLICATION_JSON))  //接收的类型
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (NestedServletException e) {
            System.out.println(e.getMessage());
            if (!(e.getCause() instanceof CustomUnauthorizedException)) {
                Assert.fail();
            }
        }

    }

    /**
     * 当提供错误用户名错误密码时抛出NestedServletException异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldThrowNSEWhenGiveLoginWithErrNamePasswd() throws Exception {
        try {
            mvc.perform(MockMvcRequestBuilders.post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"account\":\"123\",\"password\":\"123\"}")
                    .accept(MediaType.APPLICATION_JSON))  //接收的类型
                    .andExpect(status().isOk());
            Assert.fail();
        } catch (NestedServletException e) {
            System.out.println(e.getMessage());
            if (!(e.getCause() instanceof CustomUnauthorizedException)) {
                Assert.fail();
            }
        }
    }

    /**
     * 登录后验证登录
     *
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 06:27:59
     */
    @Test
    public void shouldReturnOkWhenLoginSuccess() throws Exception {
        String token =logIn();
        mvc.perform(MockMvcRequestBuilders.get("/user/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization",token)
                .header("Access-Control-Expose-Headers","Authorization"))
                .andExpect(status().isOk());
    }

    /**
     * 当发送put请求时应当改变密码
     *
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 06:27:59
     */
    @Test
    @Transactional
    public void shouldChangePasswordWhenSendPut() throws Exception {
        String token =logIn();
        mvc.perform(MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization",token)
                .header("Access-Control-Expose-Headers","Authorization")
                .content("{\"account\":\"admin\",\"password\":\"123\"}")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().isOk());
    }

    /**
     * 通过向redis中写入数据模拟登录
     *
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 03:42:35
     * @return : token
     */
    private String  logIn () {
        Long timeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign("admin", timeMillis, "userRealmTest");
        SecurityUtils.setSecurityManager(securityManager);
        RedisUtil.set(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + "admin",
                timeMillis, Integer.parseInt(accessTokenExpireTime));
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JwtToken(token));
        return token;
    }

}
