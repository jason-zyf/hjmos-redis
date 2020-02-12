package com.pci.hjmos.redis;

import com.pci.hjmos.redis.api.RedisApiService;
import com.pci.hjmos.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class,args);
    }

    @GetMapping("/index")
    public String index(){
        return "redisIndex";
    }

    /*@Resource
    RedisApiService redisService;
    @GetMapping("/hha")
    public String test(){
        redisService.set("a", "测试一下框架搭建");
        return redisService.get("a").toString();
    }*/

    @Autowired
    private RedisService redisService;

    @GetMapping("/hhadd")
    public String test(){
        redisService.set("a", "测试一下框架搭建接口");
        return redisService.get("a").toString();
    }

}
