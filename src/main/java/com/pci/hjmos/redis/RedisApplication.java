package com.pci.hjmos.redis;

import com.pci.hjmos.redis.api.RedisApiService;
import com.pci.hjmos.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@EnableAsync
@EnableHystrix
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class,args);
    }

    @GetMapping("/index")
    public String index(){
        return "redisIndex";
    }

    // 测试从配置中心获取数据
    /*@Value("${spring.redis.host}")
    String host;

    @Value("${spring.redis.port}")
    String port;

    @RequestMapping(value="/findConfig")
    public String findConfig(){
        return host+":"+port;
    }*/

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
