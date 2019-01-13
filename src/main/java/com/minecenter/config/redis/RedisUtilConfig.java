package com.minecenter.config.redis;

import com.minecenter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by asus-pc on 2019/1/13.
 */
@Configuration
public class RedisUtilConfig {

    /**
     * redisTemplate 关闭事务
     */
    private RedisTemplate<String, Object> redisTemplateNoTransaction;

    /**
     * redisTemplate 开启事务
     */
    private RedisTemplate<String, Object> redisTemplateTransaction;

    @Autowired
    public RedisUtilConfig(RedisTemplate<String, Object> redisTemplateNoTransaction,
                     @Qualifier("redisTemplateTransaction")
                             RedisTemplate<String, Object> redisTemplateTransaction) {
        this.redisTemplateNoTransaction = redisTemplateNoTransaction;
        this.redisTemplateTransaction = redisTemplateTransaction;
    }

    @Bean
    @Primary
    public RedisUtil getRedisTemplateNoTransaction(){
        return new RedisUtil(redisTemplateNoTransaction);
    }

    @Bean(name = "redisUtileNoTransaction")
    public RedisUtil getRedisTemplateTransaction(){
        return new RedisUtil(redisTemplateTransaction);
    }
}
