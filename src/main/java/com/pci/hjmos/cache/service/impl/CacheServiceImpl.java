package com.pci.hjmos.cache.service.impl;

import com.pci.hjmos.cache.service.CacheService;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

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

    //无序set操作
    @Override
    public Long add(String key ,String ...values){
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long remove(String key,Object ...values){
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public Boolean isMember(String key,Object value){
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Set<Object> members(String key){
        return redisTemplate.opsForSet().members(key);
    }

    //有序zset操作
    @Override
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Double incrementScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public Long batchAddZset(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    @Override
    public Long removeZset(String key, String... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public void zremoveByScore(String key, double min, double max) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long rank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    @Override
    public Long reverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    @Override
    public Set<Object> range(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public void expireKey(String key, long expireTime) {
        redisTemplate.expire(key,expireTime, TimeUnit.SECONDS);
    }

    /*@Override
    public Set<String> fuzzyKey(String key) {
        String str = key+"*";
        return redisTemplate.keys(key);
    }
    @Override
    public Set<String> keys() {
        String prefix = "*";
        return redisTemplate.keys(prefix);
    }*/

    @Override
    public Boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long batchDelete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public void selectDatabase(int indexDB) {
        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        assert jedisConnectionFactory != null;
        jedisConnectionFactory.setDatabase(indexDB);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        jedisConnectionFactory.resetConnection();
    }

    @Override
    public boolean setLock(String key, long expire) {
        RedisCallback<String> callback = (connection) -> {
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            String uuid = UUID.randomUUID().toString();
            // EX = seconds; PX = milliseconds 单位不同
            return commands.set(key, uuid, "NX", "PX", expire);
        };
        String result = redisTemplate.execute(callback);
        return !StringUtils.isEmpty(result);
    }

    private static final String UNLOCK_LUA;
    static {
        String sb = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                "then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end ";
        UNLOCK_LUA = sb;
    }
    @Override
    public boolean releaseDistributedLock(String key, String requestId) {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> args = new ArrayList<>();
        args.add(requestId);
        // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
        // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
        RedisCallback<Long> callback = (connection) -> {
            Object nativeConnection = connection.getNativeConnection();
            // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
            // 集群模式
            if (nativeConnection instanceof JedisCluster) {
                return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
            }

            // 单机模式
            else if (nativeConnection instanceof Jedis) {
                return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
            }
            return 0L;
        };
        Long result = redisTemplate.execute(callback);
        return result != null && result > 0;
    }
}
