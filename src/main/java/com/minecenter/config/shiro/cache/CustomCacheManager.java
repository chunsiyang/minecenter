package com.minecenter.config.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 重写Shiro缓存管理器
 * @author chunsiyang
 * @date 2018/9/4 17:41
 */
public class CustomCacheManager implements CacheManager {

    /**
     * fast lookup by name map
     */
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();


    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        Cache cache = caches.get(s);

        if (cache == null) {
            cache = new CustomCache<K,V>();
            caches.put(s,cache);
        }
        return cache;
    }
}
