package com.pci.hjmos.redis.service;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface RedisService {

    /**
     * --------------------------------- String 操作---------------------------------
     */
    /**
     * 增加接口
     * @param key 缓存key
     * @param value 缓存值
     */
    void set(String key, Object value);

    /**
     * 增加接口,附带过期时间
     * @param key 缓存key
     * @param value 缓存值
     * @param expireTime  存活时间(秒s)
     */
    void set(String key, Object value, long expireTime);

    /**
     * 批量添加
     * @param maps 键值对集合
     */
    void multiSet(Map<String, Object> maps);

    /**
     * 获取值接口 String类型
     * @param key 缓存key
     * @return 缓存值
     */
    Object get(String key);

    /**
     * 批量获取
     * @param keys 缓存key集合
     * @return 值集合
     */
    List<Object> multiGet(Collection<String> keys);

    /**
     * 删除
     * @param key 缓存key
     * @return 是否删除成功
     */
    Boolean delete(String key);

    /**
     * --------------------------------- Hash 操作---------------------------------
     */
    /**
     * 向key的hash 中存hashKey属性值为hashValue
     * @param key 缓存key
     * @param hashKey 属性名称
     * @param  hashValue 属性值
     */
    void hPut(String key, String hashKey, Object hashValue) ;

    /**
     * 批量更新hash
     * @param key hash的key
     * @param m 属性键值对
     */
    void hMultiSet(String key, Map<? extends Object, ? extends Object> m);

    /**
     * 获取hash对象中的某个属性值
     * @param key 缓存key
     * @param hashKey 属性名称
     * @return 对象的属性值
     */
    Object hGet(String key, String hashKey);

    /**
     * 删除 key的hash 中hashKey属性
     * @param key 缓存key
     * @param hashKey 属性名称
     */
    void hRemove(String key, String hashKey);

    /**
     * 获取 key的hash 中所有属性
     * @param key 缓存key
     * @return 对象的属性集合
     */
    Map<Object, Object> hEntries(String key) ;

    /**
     * key的hash 中是否存在hashKey属性
     * @param key 缓存key
     * @param hashKey 属性名称
     * @return 是否存在标识
     */
    Boolean hExists(String key, String hashKey);

    /**
     * --------------------------------- List 操作 ---------------------------------
     */
    /**
     * 向集合中添加元素
     * @param key 缓存key
     * @param value 添加的元素
     * @return 返回新增后list集合的元素个数
     */
    Long lSet(String key, Object value);

    /**
     * 向集合中添加元素+存活时间
     * @param key 缓存key
     * @param value 添加的元素
     * @param time 存活时间(秒s)
     * @return 返回新增后list集合的元素个数
     */
    Long lSet(String key, Object value, long time);

    /**
     * 批量新增集合
     * @param key 缓存key
     * @param values 添加的元素
     * @return 返回新增后list集合的元素个数
     */
    Long lMultiSet(String key, List<Object> values);
    /**
     * 批量新增集合 先清理再添加
     * @param key 缓存key
     * @param values 添加的元素
     * @param isClear 是否先清理再添加
     * @return 返回新增后list集合的元素个数
     */
    Long lMultiSet(String key, List<Object> values, boolean isClear);
    /**
     * 批量新增集合 先清理再添加
     * @param key 缓存key
     * @param values 添加的元素
     * @param isClear 是否先清理再添加
     * @param time 存活时间(秒s)
     * @return 返回新增后list集合的元素个数
     */
    Long lMultiSet(String key, List<Object> values, boolean isClear, long time);

    /**
     * 移除集合中的 某个元素
     * @param key 缓存key
     * @param value 删除的元素
     * @return 返回成功删除元素的个数，0表示没有删除元素
     */
    Long lRemove(String key,Object value);

    /**
     * 获取整个集合
     * @param key 缓存key
     * @return 集合
     */
    List<Object> lGet(String key);

    /**
     * 获取片段集合（闭区间）
     * @param key 缓存key
     * @param start 开始节点,第一个元素为0
     * @param end 结束节点
     * @return 集合
     */
    List<Object> lGet(String key, long start, long end) ;

    /**
     *  --------------------------------- Set 操作---------------------------------
     */
    /**
     * 添加 set 元素
     * @param key  缓存键
     * @param values  缓存值（一个或多个）
     * @return 返回元素新增成功个数
     */
    Long add(String key ,String ...values);
    /**
     * 删除一个或多个集合中的指定值
     * @param key  缓存键
     * @param values 缓存值（一个或多个）
     * @return 成功删除数量
     */
    Long remove(String key,Object ...values);
    /**
     * 判断 set 集合中 是否有 value
     * @param key 缓存键
     * @param value  缓存值
     * @return  是否存在标识 true：存在  false：不存在
     */
    Boolean isMember(String key,Object value);
    /**
     * 查询集合中所有元素
     * @param key 缓存键
     * @return 集合中所有元素
     */
    Set<Object> members(String key);

    /**
     * --------------------------------- ZSet 操作---------------------------------
     */
    /**
     * 添加 ZSet 元素
     * @param key  缓存键
     * @param value  缓存值
     * @param score  缓存值的分数
     */
    void zAdd(String key, Object value, double score);

    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @param key  缓存键
     * @param value  缓存值
     * @param score 加减的分数
     * @return  操作后value的分数
     */
    Double incrementScore(String key,Object value,double score);
    /**
     * 批量添加
     * @param key  缓存键
     * @param tuples
     * @return  返回添加成功个数
     */
    Long batchAddZset(String key,Set<ZSetOperations.TypedTuple<Object>> tuples);
    /**
     * Zset 删除一个或多个元素
     * @param key  缓存键
     * @param values  删除的缓存值（一个或多个）
     * @return  删除成功个数
     */
    Long removeZset(String key,String ...values);
    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )（闭区间）
     * @param key  缓存键
     * @param min  分数最小值
     * @param max   分数最大值
     */
    void zremoveByScore(String key, double min, double max);
    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @param key  缓存键
     * @param value  缓存值
     * @return  排名
     */
    Long rank(String key,Object value);

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @param key  缓存键
     * @param value  缓存值
     * @return  排名
     */
    Long reverseRank(String key,Object value);
    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)  闭区间
     * @param key  缓存键
     * @param start  开始节点
     * @param end   结束节点，若为-1，即到最后一个元素
     * @return  集合
     */
    Set<Object> range(String key, long start, long end);
    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @param key  缓存键
     * @param min  最小分数
     * @param max  最大分数
     * @return  此分数闭区间内的元素列名集合
     */
    Set<Object> rangeByScore(String key, double min, double max);


    /**
     * ---------------------------------(通用) ---------------------------------
     */
    /**
     * 设置过期时间 单位（秒）
     * @param key  缓存键
     * @param expireTime  过期时间
     */
    void expireKey(String key, long expireTime);

    /**
     * 键值模糊查询，以key字符串开头的所有键值集合
     * @param key
     * @return
     */
//    Set<String> fuzzyKey(String key);

    /**
     * 获取所有key 的集合
     * 先注释掉，模糊匹配功能使用非常方便也很强大，在小数据量情况下使用没什么问题，数据量大会导致Redis锁住及CPU飙升
     * @return
     */
//    Set<String> keys();

    /**
     * 判断key是否存在
     * @param key 缓存key
     * @return 是否存在，true：存在  false：不存在
     */
    Boolean existsKey(String key);

    /**
     * 批量删除
     * @param keys  删除key的集合
     * @return  成功删除的个数
     */
    Long batchDelete(Collection<String> keys);

    /**
     * 切换数据库
     * @param indexDB  数据库地址
     */
    void selectDatabase(int indexDB);

    /**
     * 分布式锁获取 锁
     * @param key 锁
     * @param expire 时间  ;这里设置时间是为了防止死锁，超时自动解锁
     * @return 是否获取到
     */
    boolean setLock(String key, long expire);

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    boolean releaseDistributedLock(String lockKey, String requestId);
}
