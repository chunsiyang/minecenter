package com.minecenter.api;

import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.util.AuthorizationUtil;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    @Qualifier("shiroFilter")
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    private MockMvc mvc;


    @Value("${accessTokenExpireTime}")
    private String accessTokenExpireTime;

    @Before
    public void setUp() {

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(shiroFilterFactoryBean.getFilters().get("jwt"), "/ *")
                .build();  //构造MockMvc
        SecurityUtils.setSecurityManager(securityManager);
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
     * 当提供正确用户名密码时返回200状态
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldReturn200GiveLoginWithRightNamePasswd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Origin", "")
                .header("Access-Control-Request-Headers", "Authorization")
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
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Origin", "")
                .header("Access-Control-Request-Headers", "Authorization")
                .content("{\"account\":\"admin\",\"password\":\"123\"}")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().is4xxClientError());
    }

    /**
     * 当提供错误用户名错误密码时抛出NestedServletException异常
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldThrowNSEWhenGiveLoginWithErrNamePasswd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"account\":\"123\",\"password\":\"123\"}")
                .header("Origin", "")
                .header("Access-Control-Request-Headers", "Authorization")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().is4xxClientError());
    }

    /**
     * 登录后验证登录
     *
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 06:27:59
     */
    @Test
    public void shouldReturnOkWhenLoginSuccess() throws Exception {
        String token = logIn();
        mvc.perform(MockMvcRequestBuilders.get("/user/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", AuthorizationUtil.getBearerToken(token))
                .header("Origin", "")
                .header("Access-Control-Request-Headers", "Authorization"))
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
        String token = logIn();
        mvc.perform(MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", AuthorizationUtil.getBearerToken(token))
                .header("Access-Control-Request-Headers", "Authorization")
                .header("Origin", "")
                .content("{\"account\":\"admin\",\"password\":\"123\"}")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().isOk());
    }

    /**
     * 注销后不可访问
     *
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 06:27:59
     */
    @Test
    public void shouldNotAccessWhenLogout() throws Exception {
        String token = logIn();
        mvc.perform(MockMvcRequestBuilders.get("/user/logout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", AuthorizationUtil.getBearerToken(token))
                .header("Access-Control-Request-Headers", "Authorization")
                .header("Origin", ""))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/user/article")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", AuthorizationUtil.getBearerToken(token))
                .header("Access-Control-Request-Headers", "Authorization")
                .header("Origin", ""))
                .andExpect(status().is4xxClientError());
    }
    /**
     * 通过向redis中写入数据模拟登录
     *
     * @return : token
     * @author : chunsiyang
     * @date : 2018年12月25日 下午 03:42:35
     */
    private String logIn() {
        final String testUserAccount = "admin";
        Long timeMillis = System.currentTimeMillis();
        String token = JwtUtil.sign(testUserAccount, timeMillis, "userRealmTest");
        SecurityUtils.setSecurityManager(securityManager);
        redisUtil.set(RedisKeyEnum.PREFIX_SHIRO_REFRESH_TOKEN + testUserAccount,
                timeMillis, Integer.parseInt(accessTokenExpireTime));
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(new JwtToken(token));
        return token;
    }

}
