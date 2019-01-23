package com.minecenter.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableCaching()
@PropertySource("classpath:application.yml")
public class RedisCacheConfig {

}
