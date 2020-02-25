package com.pci.hjmos.cache.service.impl;

import com.pci.hjmos.cache.service.CacheService1;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl1 implements CacheService1 {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value, long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void multiSet(Map<String, Object> maps) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.multiSet(maps);
    }

    @Override
    public Object get(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    @Override
    public List<Object> multiGet(Collection<String> keys) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.multiGet(keys);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long batchDelete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public void expireKey(String key, long expireTime) {
        redisTemplate.expire(key,expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
