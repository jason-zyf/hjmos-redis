package com.pci.hjmos.redis.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.*;
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
    //无序set操作
    /**
     * 添加 set 元素
     * @param key
     * @param values
     * @return
     */
    public Long add(String key ,String ...values){
        return redisTemplate.opsForSet().add(key, values);
    }
    /**
     * 删除一个或多个集合中的指定值
     * @param key
     * @param values
     * @return 成功删除数量
     */
    public Long remove(String key,Object ...values){
        return redisTemplate.opsForSet().remove(key, values);
    }
    /**
     * 判断 set 集合中 是否有 value
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key,Object value){
        return redisTemplate.opsForSet().isMember(key, value);
    }
    /**
     * 返回集合中所有元素
     * @param key
     * @return
     */
    public Set<Object> members(String key){
        return redisTemplate.opsForSet().members(key);
    }


    // 有序set操作
    /**
     * 添加 ZSet 元素
     * @param key
     * @param value
     * @param score
     */
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }
    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @param key
     * @param value
     * @param score 加的分数
     * @return
     */
    public Double incrementScore(String key,Object value,double score){
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }
    /**
     * 批量添加 Zset <br>
     *         Set<TypedTuple<Object>> tuples = new HashSet<>();<br>
     *         TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);<br>
     *         tuples.add(objectTypedTuple1);
     * @param key
     * @param tuples
     * @return
     */
    public Long batchAddZset(String key,Set<ZSetOperations.TypedTuple<Object>> tuples){
        return redisTemplate.opsForZSet().add(key, tuples);
    }
    /**
     * Zset 删除一个或多个元素
     * @param key
     * @param values
     * @return
     */
    public Long removeZset(String key,String ...values){
        return redisTemplate.opsForZSet().remove(key, values);
    }
    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     * @param key
     * @param min
     * @param max
     * @return
     */
    public void zremoveByScore(String key, double min, double max) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }
    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @param key
     * @param value
     * @return
     */
    public Long rank(String key,Object value){
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @param key
     * @param value
     * @return
     */
    public Long reverseRank(String key,Object value){
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }
    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end){
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }
    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> range(String key, long start, long end){
        return redisTemplate.opsForZSet().range(key, start, end);
    }
    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> rangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }


}
