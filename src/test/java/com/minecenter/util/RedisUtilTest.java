package com.minecenter.util;

import com.minecenter.model.entry.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 在事务中数据的查询应该返回null
     * 并且readonly操作正常访问
     *
     * @author chunsiyang
     * @date 2019/1/14 21:51
     */
    @Test
    @Transactional
    public void readonlyShouldSuccessWhenUseTransactional() {
        redisUtil.initConnection(true);
        redisUtil.set("test", "testVal");
        Assert.assertNull(redisUtil.get("test"));
        Assert.assertEquals(new Long(0), redisUtil.dbSize());
    }

    /**
     * 在事务中数据的查询应该返回null
     * 并且统一提交事务
     *
     * @author chunsiyang
     * @date 2019/1/14 21:51
     */
    @Test
    public void shouldCommitTogetherWhenUseMulti() {
        redisUtil.removeAll();
        redisUtil.set("abc", "cba");
        redisUtil.multi();
        redisUtil.set("123", "321");
        Assert.assertNull(redisUtil.dbSize());
        Assert.assertNull(redisUtil.get("123"));
        Assert.assertNull(redisUtil.get("cba"));
        redisUtil.exec();
        Assert.assertEquals(new Long(2), redisUtil.dbSize());
        Assert.assertEquals("321", redisUtil.get("123"));
        Assert.assertEquals("cba", redisUtil.get("abc"));
        redisUtil.removeAll();
    }

    /**
     * 事务丢弃后redis中不应该有数据
     *
     * @author chunsiyang
     * @date 2019/1/14 21:51
     */
    @Test
    public void shouldHaveNoDataWhenDiscard() {
        redisUtil.multi();
        redisUtil.set("123", "321");
        redisUtil.discard();
        Assert.assertNull(redisUtil.get("123"));
    }

    /**
     * 应当从redis中取出和插入是一样的对象
     *
     * @author : chunsiyang
     * @date : 2018年12月16日 下午 21:27:59
     */
    @Test
    public void shouldGotSameObjectFromRedis() throws Exception {
        User user = new User();
        user.setId("123");
        user.setAccount("123");
        redisUtil.set("aaa", user, 10);
        User userFromRedis = (User) redisUtil.get("aaa");
        Assert.assertEquals(user.getAccount(), userFromRedis.getAccount());
        Assert.assertEquals(user.getId(), userFromRedis.getId());
        redisUtil.removeAll();
    }
}
