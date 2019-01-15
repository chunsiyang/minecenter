package com.minecenter.config.redis;

import com.minecenter.util.RedisUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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
     * 应当正确访问redis服务
     *
     * @author : chunsiyang
     * @date : 2018年12月16日 下午 21:27:59
     */
    @Test
    public void shouldAccessRedisSuccess() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111",10);
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa").trim());
        redisUtil.removeAll();
    }
}
