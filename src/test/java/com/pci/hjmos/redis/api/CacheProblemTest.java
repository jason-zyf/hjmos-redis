package com.pci.hjmos.redis.api;

import com.pci.hjmos.redis.RedisApplication;
import com.pci.hjmos.redis.service.CacheBreakdownService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApplication.class})
@Slf4j
public class CacheProblemTest {

    @Autowired
    private CacheBreakdownService cacheBreakdownService;

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


}
