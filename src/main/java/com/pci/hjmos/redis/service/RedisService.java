package com.pci.hjmos.redis.service;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface RedisService {
    /**
     * --------String--------star
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
     */
    Object get(String key);

    /**
     * 批量获取
     * @param keys 缓存key集合
     * @return 值集合
     */
    List<Object> multiGet(Collection<String> keys);

    /**
     * --------String--------end
     * 删除
     * @param key 缓存key
     */
    Boolean delete(String key);

    /**
     * --------Hash--------start
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
     * key的hash 中是否存在hashKey属性
     * @param key 缓存key
     * @param hashKey 属性名称
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
     */
    Map<Object, Object> hEntries(String key) ;

    /**
     * * --------Hash--------end
     * key的hash 中是否存在hashKey属性
     * @param key 缓存key
     * @param hashKey 属性名称
     */
    Boolean hExists(String key, String hashKey);

    /**
     * --------List--------start
     * 向集合中添加元素
     * @param key 缓存key
     * @param value 添加的元素
     * @return 成功返回 1，如果元素已经在集合中返回 0
     */
    Long lSet(String key, Object value);

    /**
     * 向集合中添加元素+存活时间
     * @param key 缓存key
     * @param value 添加的元素
     * @param time 存活时间(秒s)
     * @return 成功返回 1，如果元素已经在集合中返回 0
     */
    Long lSet(String key, Object value, long time);

    /**
     * 批量新增集合
     * @param key 缓存key
     * @param values 添加的元素
     * @return 成功返回 1，如果元素已经在集合中返回 0
     */
    Long lMultiSet(String key, List<Object> values);
    /**
     * 批量新增集合 先清理再添加
     * @param key 缓存key
     * @param values 添加的元素
     * @return 成功返回 1，如果元素已经在集合中返回 0
     */
    Long lMultiSet(String key, List<Object> values, boolean isClear);
    /**
     * 批量新增集合 先清理再添加
     * @param key 缓存key
     * @param values 添加的元素
     * @param time 存活时间(秒s)
     * @return 成功返回 1
     */
    Long lMultiSet(String key, List<Object> values, boolean isClear, long time);

    /**
     * 移除集合中的 某个元素
     * @param key 缓存key
     * @param value 添加的元素
     * @return 成功返回 1
     */
    Long lRemove(String key,Object value);

    /**
     * 获取整个集合
     * @param key 缓存key
     * @return 集合
     */
    List<Object> lGet(String key);

    /**
     * --------List--------end
     * 获取片段集合
     * @param key 缓存key
     * @param start 开始节点
     * @param end 结束节点
     * @return 集合
     */
    List<Object> lGet(String key, long start, long end) ;

    /**
     * --------Set--------start
     * 添加 set 元素
     * @param key
     * @param values
     * @return
     */
    Long add(String key ,String ...values);
    /**
     * 删除一个或多个集合中的指定值
     * @param key
     * @param values
     * @return 成功删除数量
     */
    Long remove(String key,Object ...values);
    /**
     * 判断 set 集合中 是否有 value
     * @param key
     * @param value
     * @return
     */
    Boolean isMember(String key,Object value);
    /**
     * --------Set--------end
     * 返回集合中所有元素
     * @param key
     * @return
     */
    Set<Object> members(String key);

    /**
     * --------ZSet--------start
     * 添加 ZSet 元素
     * @param key
     * @param value
     * @param score
     */
    void zAdd(String key, Object value, double score);

    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @param key
     * @param value
     * @param score 加的分数
     * @return
     */
    Double incrementScore(String key,Object value,double score);
    /**
     * 批量添加 Zset <br>
     *         Set<TypedTuple<Object>> tuples = new HashSet<>();<br>
     *         TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);<br>
     *         tuples.add(objectTypedTuple1);
     * @param key
     * @param tuples
     * @return
     */
    Long batchAddZset(String key,Set<ZSetOperations.TypedTuple<Object>> tuples);
    /**
     * Zset 删除一个或多个元素
     * @param key
     * @param values
     * @return
     */
    Long removeZset(String key,String ...values);
    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     * @param key
     * @param min
     * @param max
     * @return
     */
    void zremoveByScore(String key, double min, double max);
    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @param key
     * @param value
     * @return
     */
    Long rank(String key,Object value);

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @param key
     * @param value
     * @return
     */
    Long reverseRank(String key,Object value);
    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<Object> range(String key, long start, long end);
    /**
     * --------ZSet--------end
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<Object> rangeByScore(String key, double min, double max);


    /**
     * (通用) 设置过期时间 单位（秒）
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
     * String 类型 判断key是否存在
     * @param key 缓存key
     * @return
     */
    Boolean existsKey(String key);

    /**
     * 批量删除
     * @param keys  删除key的集合
     * @return  删除的个数
     */
    Long batchDelete(Collection<String> keys);


}
