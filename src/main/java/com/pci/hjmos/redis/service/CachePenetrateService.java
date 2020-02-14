package com.pci.hjmos.redis.service;

import org.springframework.stereotype.Component;

@Component
public interface CachePenetrateService {

    /**
     * 缓存穿透 设置空对象
     * @param key 缓存键
     * @return
     */
    String cachePenetrate_1(String key);

    /**
     * 缓存穿透 布隆过滤器
     * @param key
     * @return
     */
    String cachePenetrate_2(String key);

}
