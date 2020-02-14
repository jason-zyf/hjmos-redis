package com.pci.hjmos.redis.service.impl;

import com.pci.hjmos.redis.common.RedisConstant;
import com.pci.hjmos.redis.service.CachePenetrateService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CachePenetrateServiceImpl implements CachePenetrateService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存穿透 设置空对象
     * @param key 缓存键
     * @return
     */
    @Override
    public String cachePenetrate_1(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if ("".equals(value)){
            return null;
        }
        if (value!=null){
            return  value.toString();
        }
        String dataBaseValue = "数据库中的值";
        System.out.println("空对象 -- 获取数据库中的值");
        if (dataBaseValue==null){
            redisTemplate.opsForValue().set(key,"",60, TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key,dataBaseValue,60,TimeUnit.MINUTES);
        }
        return dataBaseValue;
    }

    /**
     * 缓存穿透 布隆过滤器
     * @param key 缓存键
     * @return
     */
    @Override
    public String cachePenetrate_2(String key) {
        // 先判断这个key值是否存在
        if(RedisConstant.bloomFilter.mightContain(key)){
            Object value = redisTemplate.opsForValue().get(key);
            if(value != null){
                return value.toString();
            }
            String dataBaseValue = "数据库中的值";
            System.out.println("布隆过滤器 -- 获取数据库中的值");
            redisTemplate.opsForValue().set(key, dataBaseValue,60,TimeUnit.MINUTES);
            return dataBaseValue;
        }else {
            System.out.println("该key不存在");
            return null;
        }
    }


}
