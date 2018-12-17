package com.minecenter.config.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 应当正确访问redis服务
     *
     * @author : chunsiyang
     * @date : 2018年12月16日 下午 21:27:59
     */
    @Test
    public void shouldAccessRedisSuccess() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111",10);
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
}
