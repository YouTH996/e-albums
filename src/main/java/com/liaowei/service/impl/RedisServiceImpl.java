package com.liaowei.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhan Xinjian
 * @date 2020/2/24 18:55
 * <p></p>
 */
@Repository("redisService")
public class RedisServiceImpl {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 校验rediskey是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        return  redisTemplate.hasKey(key);
    }
    /**
     * 写入Redis，当前实例对象T,同时转成Json格式
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    /**
     * 写入Redis，字符串对象
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 写入Redis，当前实例对象T, 同时转成Json格式，并设置失效时间
     *
     * @param key
     * @param value
     * @param time  秒级别的
     * @param <T>
     */
    public <T> void setex(String key, T value, int time) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, TimeUnit.SECONDS);
    }

    /**
     * 写入Redis，字符串对象，并设置失效时间
     *
     * @param key
     * @param value
     * @param time  秒级别的
     */
    public void setex(String key, String value, int time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 写Redis，对象是Object任何对象
     *
     * @param key
     * @param value
     */
    public void setObject(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    /**
     * 写Redis，对象是Object任何对象，并设置失效时间
     *
     * @param key
     * @param value
     * @param time  秒级别的
     */
    public void setexObject(final String key, final Object value,
                            final int time) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, TimeUnit.SECONDS);
    }


    /**
     * 获取Redis对象，字符串对象
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? "" : value.toString();
    }

    /**
     * 获取Redis对象，当前实例对象T
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        Object obj = getObject(key);
        if (obj == null) {
            return null;
        }
        return JSON.parseObject((String) redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 获取Redis对象，对象是Object任何对象
     *
     * @param key
     * @return
     */
    public Object getObject(final String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        return obj == null ? null : JSON.parseObject(obj.toString());
    }

    /**
     * 插入Map对象，是String类型的
     *
     * @param keyValues
     */
    public void mset(final Map<String, String> keyValues) {
        redisTemplate.opsForValue().multiSet(keyValues);
    }


    /**
     * 列表左入栈
     *
     * @param key
     * @param value
     * @return
     */
    public long lpush(final String key, final String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 列表右入栈
     *
     * @param key
     * @param value
     * @return
     */
    public long rpush(final String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lSetIndex(String key, long index, Object value) {

        try {

            redisTemplate.opsForList().set(key, index, value);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lGetIndex(String key, long index, Class<T> type) {
        Object obj = redisTemplate.opsForList().index(key, index);
        return JSON.parseObject(((String) obj), type);
    }

    /**
     * 列表左出栈
     *
     * @param key
     * @return
     */
    public String lpop(final String key) {
        return (String) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 列表右出栈
     *
     * @param key
     */
    public String rpop(final String key) {
        return (String) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取列表范围数据
     *
     * @param key
     */
    public List<String> lrange(final String key, final long start,
                               final long end) {
        return ((List) redisTemplate.opsForList().range(key, start, end));
    }

    /**
     * 插入Hash对象，单个插入
     *
     * @param key
     * @param field
     * @param value
     */
    public void hsetObject(final String key, final String field, final Object value) {
        redisTemplate.opsForHash().put(key, field, JSON.toJSONString(value));
    }

    /**
     * 获取Hash对象的值，根据key找到Hash，再根据field找到对应value
     *
     * @param key
     * @param field
     * @return
     */
    public Object hgetObject(final String key, final String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 根据key获取Hash对象
     *
     * @param key
     * @return
     */
    public Map<String, Object> hgetAllObjects(final String key) {
        HashOperations<String, String, Object> ho = redisTemplate.opsForHash();
        return ho.entries(key);
    }

    /**
     * 删除对应Hash的对应field
     *
     * @param key
     * @param field
     */
    public void hdelObject(final String key, final String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * 存入set集合，将set按数组存入
     *
     * @param key
     * @param member 可变参数，例如string[]
     */
    public void sadd(final String key, final String... member) {
        redisTemplate.opsForSet().add(key, member);
    }

    /**
     * 获取set集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 移除集合中多个value值
     *
     * @param key
     * @param member
     * @return
     */
    public Long removeSet(final String key, final String... member) {
        return redisTemplate.opsForSet().remove(key, member);
    }

    /**
     * 移除对象
     *
     * @param key
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置key的失效时间
     *
     * @param key
     * @param time 秒级别的
     */
    public void expire(final String key, final int time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取模糊匹配的key
     *
     * @param pattern
     * @return
     */
    public Set<String> getKeys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取所有key
     *
     * @return
     */
    public Set<String> getAllKey() {
        return redisTemplate.keys("*");
    }

    /**
     * 获取有多少key
     *
     * @return
     */
    public Long dbSize() {
        return redisTemplate.getConnectionFactory().getConnection().dbSize();
    }

    /**
     * 清空当前Redis
     */
    public void flushDB() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    /**
     * 关闭当前Redis连接
     */
    public void shutDown() {
        redisTemplate.getConnectionFactory().getConnection().close();
    }

    /**
     * 提供获取redisTemplate的入口
     *
     * @return
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}

