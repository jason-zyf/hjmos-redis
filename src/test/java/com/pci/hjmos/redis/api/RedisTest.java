package com.pci.hjmos.redis.api;

import com.pci.hjmos.redis.RedisApplication;
import com.pci.hjmos.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApplication.class})
@Slf4j
public class RedisTest {

    @Resource
    private RedisApiService redisApiService;

    @Autowired
    private RedisService redisService;

    /**
     * 设置key-value值
     */
    @Test
    public void testSetValue(){
        redisApiService.set("a", "aa");
        log.info("设置a的值，{}",redisApiService.get("a").toString());
    }

    /**
     * 设置key的过期时间
     */
    @Test
    public void testExpire(){
        String key = "a";
        redisApiService.expire(key, 10);
        log.info("成功设置过期时间");
    }

    /**
     * 设置key-value且设置key的过期时间
     */
    @Test
    public void testPutExpireKey(){
        String key = "aa";
        Long expireTime = 100L;
        redisApiService.set(key, "hhh", expireTime);
        log.info("设置key-value，且设置key的过期时间");
    }

    /**
     * 设置key的过期时间
     */
    @Test
    public void testExpireKey(){
        String key = "a";
        Long expireTime = 100L;
        redisApiService.expireKey(key, expireTime);
        log.info("成功设置过期时间");
    }

    /**
     * 获取key对应的value
     */
    @Test
    public void testGet(){
        String key = "a";
        log.info("{}的值为：{}",key,redisApiService.get(key).toString());
    }

    /**
     * 获取key集合中的value值
     */
    @Test
    public void testGetCollection(){

        Set<String> set = new HashSet<>();
        redisApiService.set("a", "aa");
        redisApiService.set("b", "bb");

        set.add("a");
        set.add("b");
        Collection<Object> objects = redisApiService.get(set);
        log.info(objects.toString());          // [aa, bb]
    }

    /**
     * 自增a的值，如果这是a的值为非数据类型，会报错
     */
    @Test
    public void testIncr(){
        redisApiService.set("a", 1);
        redisApiService.incr("a");
        log.info(redisApiService.get("a").toString());    // 2
    }

    /**
     * 模糊查询key的值
     */
    @Test
    public void testFuzzy(){
        redisApiService.set("aa", 1);
        redisApiService.set("ab", 1);
        redisApiService.set("cab", 1);

        Set<String> set = redisApiService.fuzzy("a*");
        Set<String> set2 = redisApiService.fuzzy("a.");

        log.info(set.toString());       // [ab, age, asd, a, aa]
        log.info(set2.toString());      // []
    }

    /**
     * 判断键值key是否存在
     */
    @Test
    public void testKeyExists(){
        String key = "a";
        Boolean exists = redisApiService.exists(key);
        if(exists){
            log.info("{}键存在",key);
        }else {
            log.info("{}键不存在",key);
        }
    }

    /**
     * 删除键 key
     */
    @Test
    public void testDeleteKey(){
        String key = "a";
        Boolean exists = redisApiService.delete(key);
        if(exists){
            log.info("{}键删除成功",key);
        }else {
            log.info("{}键删除不成功",key);
        }
    }

    /**
     * 批量删除key键集合
     */
    @Test
    public void testDelateKeys(){
        // [age, ab, aa]
        Set<String> keys = new HashSet<>();
        keys.add("aa");
        keys.add("ab");
        Long delNum = redisApiService.delete(keys);
        log.info("成功删除{}个key键",delNum);
        Set<String> set = redisApiService.fuzzy("a*");
        log.info(set.toString());    // [age]
    }

    /**
     * 设置hash类型数据
     */
    @Test
    public void testHashPut(){
        String key = "user";
        String hashKey = "name";
        String value = "zhangsan";
        redisApiService.hPut(key, hashKey, value);
        log.info("设置hash对象成功");
    }

    /**
     * 根据key和hashkey获取hash类型的值
     */
    @Test
    public void testHashGet(){
        String key = "user";
        String hashKey = "name";
        String value = "zhangsan";
        redisService.hPut(key, hashKey, value);
        // 先判断user对象的name属性是否存在，避免mvn test打包时先执行删除user.name而出现的空指针异常
        /*Boolean hExists = redisApiService.hExists(key, hashKey);
        if(hExists){
            Object obj = redisApiService.hGet(key, hashKey);
            log.info("获取hash类型的值，{}",obj.toString());          // zhangsan
        }else{
            log.info("获取对象的属性值失败，不存在hash类型键值{}-{}",key,hashKey);
        }*/
        Object obj = redisApiService.hGet(key, hashKey);
        log.info("获取hash类型"+key+"的"+hashKey+"属性值，{}",obj.toString());
    }

    /**
     * 根据key和hashkey删除hash类型的key键
     */
    @Test
    public void testHashRemove(){
        String key = "user";
        String hashKey = "name";
        redisApiService.hRemove(key, hashKey);
        log.info("成功删除"+key+"对象里的"+hashKey+"属性");
    }

    /**
     * 获取hash类型key的所有属性及属性值
     */
    @Test
    public void testHashEntries(){
        String key = "user";
        Map<Object, Object> objMap = redisApiService.hEntries(key);
        log.info("获取hash类型key的所有属性及属性值："+objMap.toString());   // {age=13, name=zhangshan}
    }

    /**
     * 判断hash类型数据是否存在对象的某个属性
     */
    @Test
    public void testHashExists(){
        String key = "user";
        String hashKey = "name";
        Boolean hExists = redisService.hExists(key, hashKey);
        if(hExists){
            log.info("存在hash类型键值{}-{}",key,hashKey);
        }else{
            log.info("不存在hash类型键值{}-{}",key,hashKey);
        }
    }

    /**
     * 批量设置值
     */
    @Test
    public void testMultiPut(){
        Map<String, Object> map = new HashMap<>();
        map.put("asd", "dsa");
        map.put("qwe", "ewq");
        map.put("zxc", "cxz");
        redisApiService.multiSet(map);
    }

    /**
     * 获取批量key的value值
     */
    @Test
    public void testMultiGet(){
        Set<String> set = new HashSet<>();
        set.add("asd");
        set.add("zxc");
        List<Object> list = redisApiService.multiGet(set);
        log.info(list.toString());      // [dsa, cxz]
    }

    /**
     * 批量更新hash对象里面的属性值
     */
    @Test
    public void testHashMultiPut(){
        String key = "user";
        Map<String,Object> maps = new HashMap<>();
        maps.put("age", 16);
        maps.put("sex", "男");
        redisApiService.hMultiSet(key, maps);
        log.info("批量更新对象{}中的属性值",key);
    }

    /**
     * 查询hash中多个属性的值
     */
    @Test
    public void testHashMultiGet(){
        Set<Object> hashKeys = new HashSet<>();
        String key = "user";
        hashKeys.add("age");
        hashKeys.add("sex");
        List<Object> list = redisApiService.hMultiGet(key, hashKeys);
        log.info(list.toString());           // [男, 16]
    }

    /**
     * 从列表的尾部添加数据
     */
    @Test
    public void testListPut(){
        String key = "list1";
        String value = "23";
        Long num = redisApiService.lSet(key, value);
    }

    /**
     * 删除list类型的某个value
     */
    @Test
    public void testListRemove(){
        String key = "list1";
        String value = "23";
        Long num = redisApiService.lRemove(key, value);
        log.info("删除了{}个值",num.toString());
    }

    /**
     *
     */
    /*@Test
    public void testListMultiSet(){
        List<String> values = new ArrayList<>();
        values.add("5");
        values.add("15");
        values.add("25");
        String key = "list1";
        Long num = redisApiService.lMultiSet(key, values);
        log.info("新增后，有{}个数据",num);
    }*/

    /**
     * 批量删除key
     */
    @Test
    public void testBatchDelete(){
        // [age, ab, aa]
        Set<String> keys = new HashSet<>();
        keys.add("abc");
        keys.add("abd");
        keys.add("ghfdj");
        Long delNum = redisApiService.delete(keys);  // 未删除前 [asd, abe, abd, a, abc, age]
        log.info("成功删除{}个key键",delNum);       // 成功删除2个key键
        Set<String> set = redisApiService.fuzzy("a*");  // [asd, abe, a, age]
        log.info(set.toString());    // [age]
    }

    /**
     * 所有key的集合，String类型
     */
    /*@Test
    public void testKeys(){
        Set<String> keys = redisService.keys();
        log.info("所有的key："+keys.toString());  // [b, asd, qwe, list1, a, myhashset02, zxc, mylist, age, cab, name, user]
        Set<String> set = redisApiService.fuzzy("a*");
        log.info(set.toString());   // [asd, abe, abd, a, abc, age]
    }*/



}
