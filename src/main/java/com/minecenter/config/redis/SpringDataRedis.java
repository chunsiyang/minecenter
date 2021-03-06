package com.minecenter.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class SpringDataRedis {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 实例化 RedisTemplate 对象
     *
     * @return RedisTemplate<String, Object>
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> functionDomainRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        return getStringObjectRedisTemplate(redisConnectionFactory, true);
    }

    private RedisTemplate<String, Object> getStringObjectRedisTemplate(LettuceConnectionFactory redisConnectionFactory, boolean transactionSupport) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 开启事务
        redisTemplate.setEnableTransactionSupport(false);
        // 指定序列化器
        jdkREdisSerializer(redisTemplate);
        return redisTemplate;
    }

    /**
     * 设置数据存入 redis 的序列化方式(enericJackson2JsonRedisSerializer)
     *
     * @param redisTemplate redisTemplate
     */
    private void enericJackson2JsonRedisSerializer(RedisTemplate<String, Object> redisTemplate) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    /**
     * 设置数据存入 redis 的序列化方式(Jackson2JsonRedisSerializer)
     *
     * @param redisTemplate redisTemplate
     */
    private void jackson2JsonRedisSerializer(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    }

    /**
     * 设置数据存入 redis 的序列化方式(jdkREdisSerializer)
     *
     * @param redisTemplate redisTemplate
     */
    private void jdkREdisSerializer(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    }

}
