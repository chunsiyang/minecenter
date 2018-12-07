package com.minecenter.api;

import com.minecenter.exception.CustomUnauthorizedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
    public void shouldReturn200WhenGiveLoginWithRightNamePasswd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"account\":\"admin\",\"password\":\"admin\"}")
                .accept(MediaType.APPLICATION_JSON))  //接收的类型
                .andExpect(status().isOk());
    }

    /**
     * 当提供正确用户名错误密码时返回200状态
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldReturn200WhenGiveLoginWithRightNameErrPasswd() throws Exception {
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
     * 当提供错误用户名错误密码时返回200状态
     *
     * @author : chunsiyang
     * @date : 2018年12月05日 下午 06:27:59
     */
    @Test
    public void shouldReturn200WhenGiveLoginWithErrNamePasswd() throws Exception {
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
}
