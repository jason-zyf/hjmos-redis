package com.pci.hjmos.redis.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  打包的时候可以删除，留着是为了以后的接口扩展
 */
@Component
@Log4j2
public class RedisApiService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    // 通用操作
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * put
     *
     */
    public void set(String key, Object value) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    /**
     * put
     *
     */
    public void set(String key, Object value, Long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);

        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    public void expireKey(String key, long expireTime){
        redisTemplate.expire(key,expireTime,TimeUnit.SECONDS);
    }

    /**
     * get
     *
     */
    public Object get(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * get
     *
     */
    public Collection<Object> get(Set<String> keys) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.multiGet(keys);
    }

    /**
     * 使key保存的数据增加1
     * @param key
     * @return
     */
    public Long incr(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.increment(key);
    }

    /**
     * 模糊查询
     *
     */
    public Set<String> fuzzy(String key) {
        return redisTemplate.keys(key);
    }

    /**
     * key是否存在
     *
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除key
     *
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     * @param pattern
     * @return
     */
    public Boolean deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if(keys != null && !keys.isEmpty()) {
            keys.forEach(key -> {
                redisTemplate.delete(key);
            });
        }
        return true;
    }

    /**
     * 批量删除key
     *
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Object hGet(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.get(key, hashKey);
    }

    public void hPut(String key, String hashKey, Object hashValue) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.put(key, hashKey, hashValue);
    }

    public void hRemove(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.delete(key, hashKey);
    }

    public Map<Object, Object> hEntries(String key) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.entries(key);
    }

    public Boolean hExists(String key, String hashKey) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.hasKey(key, hashKey);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 批量添加
     */
    public void multiSet(Map<String, Object> maps) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.multiSet(maps);
    }

    public List<Object> multiGet(Collection<String> keys) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.multiGet(keys);
    }

    /**
     * 发布对象消息
     *
     * @author lsk
     * @date 2018/12/12
     */
    public void publishObjectMessage(String channel, Object msg) {
        redisTemplate.convertAndSend(channel, msg);
    }

    // hash操作

    /**
     * 批量更新hash
     *
     * @param key
     * @param m
     */
    public void hMultiSet(String key, Map<? extends Object, ? extends Object> m) {
        redisTemplate.opsForHash().putAll(key, m);
    }

    public List<Object> hMultiGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    // list操作
    public Long lSet(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Long lRemove(String key,Object value){
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.remove(key,0,value);
    }

    public Long lSet(String key, Object value, long time) {
        Long result = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return result;
    }

    public  Long lMultiSet(String key, List<Object> values) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.rightPushAll(key, values);
    }

    public Long lMultiSet(String key, List<Object> values, boolean isClear) {
        if (isClear) {
            redisTemplate.delete(key);
        }
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public Long lMultiSet(String key, List<Object> values, boolean isClear, long time) {
        Long result = lMultiSet(key, values, isClear);
        expire(key, time);
        return result;
    }

    public List<Object> lGet(String key) {
        return lGet(key, 0, -1);
    }

    public List<Object> lGet(String key, long start, long end) {
        List<Object> result = redisTemplate.opsForList().range(key, start, end);
        return result;
    }

    // 有序set操作
    /*public Set<TypedTuple<Object>> zrangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    public Set<TypedTuple<Object>> zRevrangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }*/
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void zremoveByScore(String key, double min, double max) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

}
