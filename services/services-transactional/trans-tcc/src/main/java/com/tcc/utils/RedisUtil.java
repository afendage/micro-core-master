package com.tcc.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOps;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        // 确保与RedisConfig序列化配置一致
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    // 字符串操作
    public Object get(String key) {
        return valueOps.get(key);
    }

    public void set(String key, Object value) {
        valueOps.set(key, value);
    }

    public Boolean setIfAbsent(String key, Object value) {
        return valueOps.setIfAbsent(key, value);
    }

    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        valueOps.set(key, value, timeout, unit);
    }

    // 键操作
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 其他实用方法
    public Long increment(String key, long delta) {
        return valueOps.increment(key, delta);
    }

    public Long decrement(String key, long delta) {
        return valueOps.decrement(key, delta);
    }
}
