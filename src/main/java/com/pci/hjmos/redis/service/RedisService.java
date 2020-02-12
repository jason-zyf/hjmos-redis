package com.pci.hjmos.redis.service;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface RedisService {

    /**
     * 增加接口
     */
    void set(String key, Object value);

    /**
     * 获取值接口
     */
    Object get(String key);

    /**
     * 删除
     */
    Boolean delete(String key);


    /**
     *  修改,同新增接口
     */


    /**
     * 设置过期时间 单位（秒）
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
