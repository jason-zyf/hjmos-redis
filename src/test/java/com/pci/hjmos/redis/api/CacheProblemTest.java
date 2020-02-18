package com.pci.hjmos.redis.api;

import com.pci.hjmos.cache.CacheApplication;
import com.pci.hjmos.cache.service.CacheBreakdownService;
import com.pci.hjmos.cache.service.CachePenetrateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CacheApplication.class})
@Slf4j
public class CacheProblemTest {

    @Autowired
    private CacheBreakdownService cacheBreakdownService;

    @Autowired
    private CachePenetrateService cachePenetrateService;

    /**
     * 测试高并发下请求一个缓存过期的键值,线程锁的方式，解决穿透问题
     */
    @Test
    public void test1(){
        for(int i = 0;i < 500;i++){
            new Thread(()-> {
                try {
                    System.out.println(cacheBreakdownService.cacheBreakdown_1("STR5"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            System.out.println(cacheBreakdownService.cacheBreakdown_1("STR5"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2(){
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_2("STR5","LOCAL"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_2("STR6","LOCAL"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_2("STR7","LOCAL"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_2("STR8","LOCAL"));
        }).start();
        System.out.println(cacheBreakdownService.cacheBreakdown_2("STR9","LOCAL"));

    }

    /**
     * 设置缓存的随机过期时间
     */
    @Test
    public void test3(){
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_3("STR5"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_3("STR6"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_3("STR11"));
        }).start();
        new Thread(()-> {
            System.out.println(cacheBreakdownService.cacheBreakdown_3("STR10"));
        }).start();
        System.out.println(cacheBreakdownService.cacheBreakdown_3("STR9"));
    }

    @Test
    public void test4(){
        for (int i = 0;i < 100; i++){
            new Thread(()-> {
                try {
                    System.out.println(cacheBreakdownService.cacheBreakdown_4("STR5"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            System.out.println(cacheBreakdownService.cacheBreakdown_4("STR5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过布隆过滤器防止缓存穿透
     */
    @Test
    public void test5(){
        // 只有布隆过滤器中可允许的值才查询数据，否则返回该key不存在
        String key1 = cachePenetrateService.cachePenetrate_2("KEY1");
        String key2 = cachePenetrateService.cachePenetrate_2("STR1");

        log.info("布隆过滤器可通过的key返回：" + key1);
        log.info("布隆过滤器不可通过的key返回：" + key2);
    }


}
