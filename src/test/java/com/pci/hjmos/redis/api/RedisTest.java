package com.pci.hjmos.redis.api;

import com.pci.hjmos.redis.RedisApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApplication.class})
@Slf4j
public class RedisTest {

    @Resource
    private RedisApiService redisService;

    /**
     * 设置key-value值
     */
    @Test
    public void testSetValue(){
        redisService.set("a", "aa");
        log.info("设置a的值，{}",redisService.get("a").toString());
    }

    /**
     * 设置key的过期时间
     */
    @Test
    public void testExpire(){
        String key = "a";
        redisService.expire(key, 10);
        log.info("成功设置过期时间");
    }

    /**
     * 设置key-value且设置key的过期时间
     */
    @Test
    public void testPutExpireKey(){
        String key = "aa";
        Long expireTime = 100L;
        redisService.set(key, "hhh", expireTime);
        log.info("设置key-value，且设置key的过期时间");
    }

    /**
     * 设置key的过期时间
     */
    @Test
    public void testExpireKey(){
        String key = "a";
        Long expireTime = 100L;
        redisService.expireKey(key, expireTime);
        log.info("成功设置过期时间");
    }

    /**
     * 获取key对应的value
     */
    @Test
    public void testGet(){
        String key = "a";
        log.info("{}的值为：{}",key,redisService.get(key).toString());
    }

    /**
     * 获取key集合中的value值
     */
    @Test
    public void testGetCollection(){

        Set<String> set = new HashSet<>();
        redisService.set("a", "aa");
        redisService.set("b", "bb");

        set.add("a");
        set.add("b");
        Collection<Object> objects = redisService.get(set);
        log.info(objects.toString());          // [aa, bb]
    }

    /**
     * 自增a的值，如果这是a的值为非数据类型，会报错
     */
    @Test
    public void testIncr(){
        redisService.set("a", 1);
        redisService.incr("a");
        log.info(redisService.get("a").toString());    // 2
    }

    /**
     * 模糊查询key的值
     */
    @Test
    public void testFuzzy(){
        redisService.set("aa", 1);
        redisService.set("ab", 1);
        redisService.set("cab", 1);

        Set<String> set = redisService.fuzzy("a*");
        Set<String> set2 = redisService.fuzzy("a.");

        log.info(set.toString());       // [ab, age, asd, a, aa]
        log.info(set2.toString());      // []
    }

    /**
     * 判断键值key是否存在
     */
    @Test
    public void testKeyExists(){
        String key = "a";
        Boolean exists = redisService.exists(key);
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
        Boolean exists = redisService.delete(key);
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
        Long delNum = redisService.delete(keys);
        log.info("成功删除{}个key键",delNum);
        Set<String> set = redisService.fuzzy("a*");
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
        redisService.hPut(key, hashKey, value);
        log.info("设置hash对象成功");
    }

    /**
     * 根据key和hashkey获取hash类型的值
     */
    @Test
    public void testHashGet(){
        String key = "user";
        String hashKey = "name";
        // 先判断user对象的name属性是否存在，避免mvn test打包时先执行删除user.name而出现的空指针异常
        Boolean hExists = redisService.hExists(key, hashKey);
        if(hExists){
            Object obj = redisService.hGet(key, hashKey);
            log.info("获取hash类型的值，{}",obj.toString());          // zhangsan
        }else{
            log.info("获取对象的属性值失败，不存在hash类型键值{}-{}",key,hashKey);
        }
    }

    /**
     * 根据key和hashkey删除hash类型的key键
     */
    @Test
    public void testHashRemove(){
        String key = "user";
        String hashKey = "name";
        redisService.hRemove(key, hashKey);
        log.info("成功删除"+key+"对象里的"+hashKey+"属性");
    }

    /**
     * 获取hash类型key的所有属性及属性值
     */
    @Test
    public void testHashEntries(){
        String key = "user";
        Map<Object, Object> objMap = redisService.hEntries(key);
        log.info("获取hash类型key的所有属性及属性值："+objMap.toString());   // {age=13, name=zhangshan}
    }

    /**
     * 判断hash类型数据是否存在对象的某个属性
     */
    @Test
    public void testHashExists(){
        String key = "user";
        String hashKey = "name1";
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
        redisService.multiSet(map);
    }

    /**
     * 获取批量key的value值
     */
    @Test
    public void testMultiGet(){
        Set<String> set = new HashSet<>();
        set.add("asd");
        set.add("zxc");
        List<Object> list = redisService.multiGet(set);
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
        redisService.hMultiSet(key, maps);
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
        List<Object> list = redisService.hMultiGet(key, hashKeys);
        log.info(list.toString());           // [男, 16]
    }

    /**
     * 从列表的尾部添加数据
     */
    @Test
    public void testListPut(){
        String key = "list1";
        String value = "23";
        Long num = redisService.lSet(key, value);
    }

    /**
     * 删除list类型的某个value
     */
    @Test
    public void testListRemove(){
        String key = "list1";
        String value = "23";
        Long num = redisService.lRemove(key, value);
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
        Long num = redisService.lMultiSet(key, values);
        log.info("新增后，有{}个数据",num);
    }*/

}
