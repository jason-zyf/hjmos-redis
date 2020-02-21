package com.pci.hjmos.cache.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.pci.hjmos.cache.service.CacheBreakdownService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CacheBreakdownServiceImpl implements CacheBreakdownService {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private Lock lock = new ReentrantLock();

    /**
     * 缓存击穿,雪崩解决，使用线程锁，保证同一时刻只能有同一种key值能够进行 db数据的获取
     * @param key 缓存键
     * @return
     * @throws InterruptedException
     */
    @Override
    public String cacheBreakdown_1(String key) throws InterruptedException {
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null){
            return value.toString()+" --> 缓存,key:"+key+",线程："+Thread.currentThread().getName();
        }
        if(lock.tryLock()){
            String dataBaseValue;
            try {
                //数据库获取，并存在redis 中
                dataBaseValue = "数据库中的值";
                System.out.println("互斥锁 -- 获取数据库中的值");
                redisTemplate.opsForValue().set(key,dataBaseValue,60,TimeUnit.MINUTES);
            }finally {
                lock.unlock();
            }
            return dataBaseValue+" --> 数据库,key:"+key+",线程："+Thread.currentThread().getName();
        }
        TimeUnit.SECONDS.sleep(2);
        return cacheBreakdown_1(key);
    }

    /**
     * 缓存击穿,雪崩解决 ，分布式锁，保证同一时刻只能有同一种key值能够进行 db数据的获取
     * @param key 缓存键
     * @return
     */
    @Override
    public String cacheBreakdown_2(String key, String localKey) {
        System.out.println("线程开始");
        Object value = redisTemplate.opsForValue().get(key);
        if (value!=null){
            return value.toString()+" --> 缓存,key:"+key+",线程："+Thread.currentThread().getName();
        }
        Boolean isSuccess=redisTemplate.opsForValue().setIfAbsent(localKey,"easy");
        System.out.println(Thread.currentThread().getName()+"isSuccess : "+isSuccess);
        if (isSuccess){
            //数据库获取信息，并放进缓存中
            String dataBaseValue = "数据库中的值";
            System.out.println("分布式锁 -- 获取数据库中的值");
            redisTemplate.opsForValue().set(key,dataBaseValue,60,TimeUnit.MINUTES);
            redisTemplate.delete(localKey);
            return dataBaseValue+" --> 数据库,key:"+key+",线程："+Thread.currentThread().getName();
        }else{
            try {
                System.out.println(Thread.currentThread().getName()+"\t开始等待");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  cacheBreakdown_2(key,localKey);
        }
    }

    /**
     * 雪崩解决 增加随机过期时间，减少缓存同时消失的概率
     * @param key  缓存键
     * @return
     */
    @Override
    @CacheEvict
    public String cacheBreakdown_3(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null){
            return value.toString()+"-->缓存";
        }
        String dataBaseValue = "数据库中的值";
        Random random = new Random(100);
        long expireTime = 60 + random.nextInt(6);
        System.out.println("随机过期时间 -- 获取数据库中的值");
        redisTemplate.opsForValue().set(key,dataBaseValue,expireTime,TimeUnit.MINUTES);
        return dataBaseValue+"-->数据库";
    }

    /**
     * 雪崩解决 , 缓存击穿 hystrix 降级操作
     * @param key 缓存键
     * @return
     * @throws Exception
     */
    @Override
    @HystrixCommand(fallbackMethod = "hystrixString")
    public String cacheBreakdown_4(String key) throws Exception {
        Object value = redisTemplate.opsForValue().get(key);
        if (value!=null){
            return value.toString() + "-->缓存";
        }
        String dataBaseValue="数据库中的值";
        System.out.println("hystrix -- 获取数据库中的值");
        redisTemplate.opsForValue().set(key,dataBaseValue,60, TimeUnit.MINUTES);
        throw new Exception("adfs");
    }

    public String hystrixString(String key){
        return "服务器繁忙！";
    }

}
