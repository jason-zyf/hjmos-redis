package com.pci.hjmos.redis.api;

import com.pci.hjmos.cache.CacheApplication;
import com.pci.hjmos.cache.service.CacheBreakdownService;
import com.pci.hjmos.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CacheApplication.class})
@Slf4j
public class CacheProblemTest {

    @Autowired
    private CacheBreakdownService cacheBreakdownService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void test1(){
        for(int i = 0;i < 100;i++){
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
    //测试雪崩 击穿  ,分布式锁方式
    @Test
    public void testBreakdown() throws InterruptedException {

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        list.parallelStream().forEach(u -> {
            try {
                System.out.println(cacheBreakdownService.cacheBreakdown_1("BreakdownKey"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(1000);
        cacheService.delete("BreakdownKey");
    }

}
