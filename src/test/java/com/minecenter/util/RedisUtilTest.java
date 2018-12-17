package com.minecenter.util;

import com.minecenter.model.entry.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {
    @Autowired
    RedisUtil redisUtil;

    /**
     * 应当从redis中取出和插入是一样的对象
     *
     * @author : chunsiyang
     * @date : 2018年12月16日 下午 21:27:59
     */
    @Test
    public void shouldGotSameObjectFromRedis() throws Exception {
        User user = new User();
        user.setId(123);
        user.setAccount("123");
        redisUtil.set("aaa", user,10);
        User userFromRedis =(User) redisUtil.get("aaa");
        Assert.assertEquals(user.getAccount(), userFromRedis.getAccount());
        Assert.assertEquals(user.getId(), userFromRedis.getId());
    }
}
