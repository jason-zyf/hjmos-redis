package com.pci.hjmos.redis.service;

import org.springframework.stereotype.Component;

@Component
public interface CacheBreakdownService {

    /**
     * 缓存击穿,雪崩解决，使用线程锁，保证同一时刻只能有同一种key值能够进行 db数据的获取
     * @param key 缓存键
     * @return
     * @throws InterruptedException
     */
    String cacheBreakdown_1(String key) throws InterruptedException;

    /**
     * 缓存击穿,雪崩解决 ，分布式锁，保证同一时刻只能有同一种key值能够进行 db数据的获取
     * @param key  缓存键
     * @param localKey
     * @return
     */
    String cacheBreakdown_2(String key,String localKey);

    /**
     * 雪崩解决 增加随机过期时间，减少缓存同时消失的概率
     * @param key  缓存键
     * @return
     */
    String cacheBreakdown_3(String key);

    /**
     * 雪崩解决 , 缓存击穿 hystrix 降级操作
     * @param key 缓存键
     * @return
     * @throws Exception
     */
//    String cacheBreakdown_4(String key) throws Exception;

}
