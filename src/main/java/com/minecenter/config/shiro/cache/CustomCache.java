package com.minecenter.config.shiro.cache;

import com.minecenter.model.common.RedisKeyEnum;
import com.minecenter.util.JwtUtil;
import com.minecenter.util.RedisUtil;
import com.minecenter.util.common.PropertiesUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 重写Shiro的Cache保存读取
 * @author chunsiyang
 * @date 2018/9/4 17:31
 */
public class CustomCache<K,V> implements Cache<K,V> {

    /**
     * 缓存的key名称获取为shiro:cache:account
     * @param key
     * @return java.lang.String
     * @author chunsiyang
     * @date 2018/9/4 18:33
     */
    private String getKey(Object key){
        return RedisKeyEnum.PREFIX_SHIRO_CACHE + JwtUtil.getSubject(key.toString());
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(!RedisUtil.exists(this.getKey(key))){
            return null;
        }
        return RedisUtil.get(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 读取配置文件，获取Redis的Shiro缓存过期时间
        PropertiesUtil.readProperties("config.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");
        // 设置Redis的Shiro缓存
        return RedisUtil.set(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(!RedisUtil.exists(this.getKey(key))){
            return null;
        }
        RedisUtil.del(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        RedisUtil.removeAll();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        Long size = RedisUtil.dbSize();
        return size.intValue();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        return  RedisUtil.keys("*");
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(RedisUtil.get(this.getKey(key)));
        }
        return values;
    }
}
