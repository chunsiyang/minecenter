package com.minecenter.aop;

import com.minecenter.annotation.CustomTransactional;
import com.minecenter.util.RedisUtil;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class CustomTransactionalIoc {

    private RedisUtil redisUtil;

    @Autowired
    public CustomTransactionalIoc(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Before("@annotation(customTransactional)")
    public void initRedisBefore(CustomTransactional customTransactional) {
        if (customTransactional.enableRedisTransactional()) {
            redisUtil.initConnection(true);
        }
    }

    @After("@annotation(customTransactional)")
    public void initRedisAfter(CustomTransactional customTransactional) {
        if (customTransactional.enableRedisTransactional()) {
            redisUtil.initConnection(false);
        }
    }
}
