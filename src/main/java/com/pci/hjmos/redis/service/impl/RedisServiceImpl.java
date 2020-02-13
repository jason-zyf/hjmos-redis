package com.pci.hjmos.redis.service.impl;

import com.pci.hjmos.redis.service.RedisService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

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
    public void hPut(String key, String hashKey, Object hashValue) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.put(key, hashKey, hashValue);
    }

    @Override
    public void hMultiSet(String key, Map<?, ?> m) {
        redisTemplate.opsForHash().putAll(key, m);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.get(key, hashKey);
    }

    @Override
    public void hRemove(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.delete(key, hashKey);
    }

    @Override
    public Map<Object, Object> hEntries(String key) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.entries(key);
    }

    @Override
    public Boolean hExists(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.hasKey(key, hashKey);
    }

    @Override
    public Long lSet(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long lSet(String key, Object value, long time) {
        Long result = redisTemplate.opsForList().rightPush(key, value);
        expireKey(key, time);
        return result;
    }

    @Override
    public Long lMultiSet(String key, List<Object> values) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.rightPushAll(key, values);
    }

    @Override
    public Long lMultiSet(String key, List<Object> values, boolean isClear) {
        if (isClear)
            redisTemplate.delete(key);
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long lMultiSet(String key, List<Object> values, boolean isClear, long time) {
        Long result = lMultiSet(key, values, isClear);
        expireKey(key, time);
        return result;
    }
    @Override
    public Long lRemove(String key, Object value) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.remove(key,0,value);
    }
    @Override
    public List<Object> lGet(String key) {
        return lGet(key, 0, -1);
    }
    @Override
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    @Override
    public void expireKey(String key, long expireTime) {
        redisTemplate.expire(key,expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> fuzzyKey(String key) {
        String str = key+"*";
        return redisTemplate.keys(key);
    }

    @Override
    public Boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long batchDelete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Set<String> keys() {
        String prefix = "*";
        return redisTemplate.keys(prefix);
    }


}
