package com.minecenter.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CacheUtil {

    @Autowired
    private static RedisUtil redisUtil;

    /**
     * 清空数据库
     *
     * @return java.lang.Boolean
     * @author chunsiyang
     * @date 2018/12/19 15:51
     */
    public static Long removeAll() {
        return redisUtil.removeAll();
    }

    //=============================common============================

    /**
     * key是否存在
     *
     * @param key key
     * @return java.lang.Boolean
     * @author chunsiyang
     * @date 2018/9/4 15:51
     */
    public static Boolean exists(String key) {
        return redisUtil.exists(key);
    }

    /**
     * 获取key数量
     *
     * @return java.lang.Boolean
     * @author chunsiyang
     * @date 2018/12/18 15:51
     */
    public static Long dbSize() {
        return redisUtil.dbSize();
    }

    /**
     * 根据正则获取匹配的key
     *
     * @param key 正则表达式
     * @return java.lang.Boolean
     * @author chunsiyang
     * @date 2018/9/4 15:51
     */
    public static Set<String> keys(String key) {
        return redisUtil.keys(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return true 成功 ， false 失败
     */
    public static boolean expire(String key, long time) {
        return redisUtil.expire(key, time);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return redisUtil.getExpire(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        redisUtil.del(key);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return redisUtil.get(key);
    }

    //============================String=============================

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        return redisUtil.set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        return redisUtil.set(key, value, time);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long 改变个数
     */
    public static long incr(String key, long delta) {
        return redisUtil.incr(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long 改变个数
     */
    public static long decr(String key, long delta) {
        return redisUtil.decr(key, delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key, String item) {
        return redisUtil.hget(key, item);
    }

    //================================Map=================================

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmget(String key) {
        return redisUtil.hmget(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hmset(String key, Map<String, Object> map) {
        return redisUtil.hmset(key, map);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String, Object> map, long time) {
        return redisUtil.hmset(key, map, time);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value) {
        return redisUtil.hset(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value, long time) {
        return redisUtil.hset(key, item, value, time);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hdel(String key, Object... item) {
        redisUtil.hdel(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        return redisUtil.hHasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return double 操作后的值
     */
    public static double hincr(String key, String item, double by) {
        return redisUtil.hincr(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return double 操作后的值
     */
    public static double hdecr(String key, String item, double by) {
        return redisUtil.hdecr(key, item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set<Object>
     */
    public static Set<Object> sGet(String key) {
        return redisUtil.sGet(key);
    }

    //============================set=============================

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        return redisUtil.sHasKey(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, Object... values) {
        return redisUtil.sSet(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key, long time, Object... values) {
        return redisUtil.sSetAndTime(key, time, values);
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return long
     */
    public static long sGetSetSize(String key) {
        return redisUtil.sGetSetSize(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        return redisUtil.setRemove(key, values);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return List<Object> list 缓存的内容
     */
    public static List<Object> lGet(String key, long start, long end) {
        return redisUtil.lGet(key, start, end);
    }
    //===============================list=================================

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return long 影响行数
     */
    public static long lGetListSize(String key) {
        return redisUtil.lGetListSize(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return Object list中对应索引的对象
     */
    public static Object lGetIndex(String key, long index) {
        return redisUtil.lGetIndex(key, index);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true 成功 ，false 失败
     */
    public static boolean lSet(String key, Object value) {
        return redisUtil.lSet(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return ture 成功 ，false 失败
     */
    public static boolean lSet(String key, Object value, long time) {
        return redisUtil.lSet(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return ture 成功 ，false 失败
     */
    public static boolean lSet(String key, List<Object> value) {
        return redisUtil.lSet(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return ture 成功 ，false 失败
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        return redisUtil.lSet(key, value);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return ture 成功 ，false 失败
     */
    public static boolean lUpdateIndex(String key, long index, Object value) {
        return redisUtil.lUpdateIndex(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        return redisUtil.lRemove(key, count, value);
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        CacheUtil.redisUtil = redisUtil;
    }
}
