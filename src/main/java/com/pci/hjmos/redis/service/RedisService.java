package com.pci.hjmos.redis.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
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
     * 获取值接口
     * @param key 缓存key
     */
    Object get(String key);

    /**
     * 批量获取
     * @param keys 缓存key集合
     * @return 值集合
     */
    public List<Object> multiGet(Collection<String> keys);

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
     * (通用) 设置过期时间 单位（秒）
     */
    void expireKey(String key, long expireTime);
    /**
     *  key的模糊查询
     */
    Set<String> fuzzyKey(String key);

    /**
     * key 是否存在
     */
    Boolean existsKey(String key);


}
