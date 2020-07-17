package com.pci.hjmos.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {

    // 通过配置中心方式，先注释
    @Value("${spring.redis.host}")
    String host;

    @Value("${spring.redis.port}")
    int port;

    @Bean(name = "defaultSerializer")
    public DefaultSerializer defaultSerializer() {
        return new DefaultSerializer();
    }

    // 原来的方式
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, DefaultSerializer defaultSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        // redis存取对象的关键配置
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // ObjectRedisSerializer类为java对象的序列化和反序列化工具类
        template.setValueSerializer(defaultSerializer);
        // 为hashvalue添加序列化和反序列化类
        template.setHashValueSerializer(defaultSerializer);
        return template;
    }

    /*@Bean
    public RedisTemplate<String, Object> redisTemplate(DefaultSerializer defaultSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getJedisConnectionFactory());
        template.afterPropertiesSet();
        // redis存取对象的关键配置
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // ObjectRedisSerializer类为java对象的序列化和反序列化工具类
        template.setValueSerializer(defaultSerializer);
        // 为hashvalue添加序列化和反序列化类
        template.setHashValueSerializer(defaultSerializer);
        return template;
    }

    @Bean
    public RedisConnectionFactory getJedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        return jedisConnectionFactory;
    }*/


}
