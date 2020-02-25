package com.pci.hjmos.cache.api;

import com.pci.hjmos.cache.CacheApplication;
import com.pci.hjmos.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CacheApplication.class})
@Slf4j
public class CacheTest {

//    @Resource
//    private RedisApiService redisApiService;

    @Autowired
    private CacheService redisService;

    /******************   String 类型   ******************/
    /**
     * String类型，新增接口测试，设置key-value值
     */
    @Test
    public void testSetValue(){
        Map<String,Object> map = new HashMap<>();
        map.put("name", "lisi");
        map.put("age", 13);

        redisService.set("config", map);
//        redisService.set("b", "测试一下");
        log.info("设置config的值，{}",redisService.get("config"));

        /**
         * 设置key-value且设置key的过期时间
         */
//        String key = "aa";
//        Long expireTime = 100L;
//        redisService.set(key, "hhh", expireTime);
//        log.info("设置key-value，且设置key的过期时间");
//
//        log.info("获取接口测试，{}的值为：{}",key,redisApiService.get(key).toString());
    }

    /**
     * String类型，批量设置值
     */
    @Test
    public void testMultiPut(){
        Map<String, Object> map = new HashMap<>();
        map.put("asd", "dsa");
        map.put("qwe", "ewq");
        map.put("zxc", "cxz");
        redisService.multiSet(map);
        log.info("string类型批量设置值接口测试");

        Set<String> keySet = new HashSet<>();
        keySet.add("asd");
        keySet.add("qwe");
        keySet.add("zxc");
        List<Object> multiGet = redisService.multiGet(keySet);
        log.info("批量获取接口测试，{}",multiGet.toString());
    }

    /**
     * String类型，删除键 key
     */
    @Test
    public void testDeleteKey(){
        String key = "a";
        redisService.set(key, "测试删除接口");
        Boolean exists = redisService.delete(key);
        if(exists){
            log.info("测试删除接口，{}键删除成功",key);
        }else {
            log.info("测试删除接口，{}键删除不成功",key);
        }
    }

   /******************   hash 类型   ******************/
    /**
     * 设置hash类型数据
     * 获取hash类型的某个属性值
     * 判断对象是否包含某个属性
     */
    @Test
    public void testHashPut(){
        String key = "user";
        String hashKey = "name";
        String value = "zhangsan";
        redisService.hPut(key, hashKey, value);
        log.info("测试hash类型新增接口，设置hash对象成功");

        Boolean hExists = redisService.hExists(key, hashKey);
        if(hExists){
            log.info("测试是否存在对象的某个属性值，存在hash类型键值{}-{}",key,hashKey);          // zhangsan
        }else{
            log.info("测试是否存在对象的某个属性值，不存在hash类型键值{}-{}",key,hashKey);
        }

        Object obj = redisService.hGet(key, hashKey);
        log.info("测试hash类型获取某个属性值接口，{}-{}：{}",key,hashKey,obj);
    }

    /**
     * 批量更新hash对象里面的属性值
     */
    @Test
    public void testHashMultiPut(){
        String key = "user";
        Map<String,Object> maps = new HashMap<>();
        maps.put("name", "zhangsan");
        maps.put("age", 16);
        maps.put("sex", "男");
        redisService.hMultiSet(key, maps);
        log.info("测试hash类型批量新增接口，批量更新对象{}中的属性值",key);

        Map<Object, Object> objMap = redisService.hEntries(key);
        log.info("测试获取hash类型key的所有属性及属性值接口："+objMap.toString()); //{sex=男, age=16, name=zhangsan}
    }

    /**
     * 根据key和hashkey删除hash类型的key键
     */
    @Test
    public void testHashRemove(){
        String key = "user";
        String hashKey = "name";
        String value = "zhangsan";
        // 先设置值再测试删除接口
        redisService.hPut(key, hashKey, value);

        redisService.hRemove(key, hashKey);
        log.info("测试hash类型删除对象某个属性，成功删除"+key+"对象里的"+hashKey+"属性");
    }

    /*****************  List 类型  ***************/
    /**
     * 从列表的尾部添加数据
     */
    @Test
    public void testListPut(){
        String key = "list1";
        String value = "23";
        String val2 = "expireTest";
        long time = 100L;
        Long num = redisService.lSet(key, value);
        log.info("测试list新增接口，新增后list有{}个元素",num);

        Long num2 = redisService.lSet(key, val2, time);
        log.info("测试list新增且设置过期时间接口，新增后list有{}个元素",num2);
    }

    /**
     *  测试批量新增接口
     *  测试删除元素接口
     */
    @Test
    public void testListMultiSet(){
        List<Object> values = new ArrayList<>();
        values.add("5");
        values.add("15");
        values.add("25");
        values.add("25");
        String key = "list1";
        Long num = redisService.lMultiSet(key, values);
        log.info("测试list批量新增接口，新增后list有{}个数据",num);

        List<Object> valList = redisService.lGet(key);
        log.info("测试list获取元素接口，{}中元素有：{}",key,valList);

        List<Object> objects = redisService.lGet(key, 2, 5);
        log.info("测试list根据起始位置获取元素接口，{}",objects);

        String value = "25";
        Long num2 = redisService.lRemove(key, value);
        log.info("测试删除元素接口，删除了{}个值",num2);
    }

    /**
     *  测试批量新增接口
     */
    @Test
    public void testDelAfterMultiSet(){
        List<Object> values = new ArrayList<>();
        values.add("5");
        values.add("15");
        values.add("25");
        String key = "list2";
        Long num = redisService.lMultiSet(key, values);
        log.info("测试list批量新增接口，新增后list有{}个数据",num);

        long time = 100L;
        List<Object> vals2 = new ArrayList<>();
        vals2.add("a");
        vals2.add("b");
        Long num2 = redisService.lMultiSet(key, vals2, false, time);
        log.info("测试list先清除元素再批量新增接口，新增后list有{}个数据",num2);
    }

    /***************  SET类型  ******************/
    /**
     * 新增元素测试
     * 删除元素测试
     */
    @Test
    public void testAddSet(){
        String key = "set1";
        String val1 = "asd";
        String val2 = "zxc";
        String val3 = "qwe";

        Long num1 = redisService.add(key, val1, val2);
        log.info("测试set类型新增接口，新增元素为{}个", num1);   // 2
        Long num2 = redisService.add(key, val3);
        log.info("测试set类型新增接口，新增元素为{}个", num2);   // 1
        Long num3 = redisService.add(key, val2,val3);
        log.info("测试set类型新增接口(元素重复)，新增元素为{}个", num3); // 0

        Long removeNum = redisService.remove(key, val1, "hhhh");
        log.info("测试set类型删除接口，删除元素为{}个", removeNum); // 1

        Boolean b2 = redisService.isMember(key, val2);
        if(b2){
            log.info("测试set类型是否存在某个元素接口，存在元素{}", val2);
        }else {
            log.info("测试set类型是否存在某个元素接口，不存在元素{}", val2);
        }

        Set<Object> members = redisService.members(key);
        log.info("测试set类型获取所有元素接口，{}中所有元素有：{}",key,members);   // [zxc, qwe]

    }

    /******************  ZSET 操作  ***************/
    @Test
    public void testZSet(){

        String key = "zset1";
        String val1 = "asd";
        double score = 2;
        redisService.zAdd(key, val1, score);
        log.info("测试ZSET类型新增接口");

        Double score1 = redisService.incrementScore(key, val1, -1);
        log.info("测试对指定的zset的value值,socre属性做增减操作,操作后分数为{}",score1);   // 6.0

//        Long rnum = redisService.removeZset(key, val1,"hhh");
//        log.info("测试ZSET类型删除接口，成功删除接口{}个",rnum);   // 1
        redisService.zAdd(key, "qwe", score);
        redisService.zAdd(key, "qwe", 6);  // 分数不同，只会修改分数，不会新增一个元素
        redisService.zAdd(key, "dfg", 8);

//        redisService.zremoveByScore(key,4,6);
//        log.info("测试ZSET类型根据分数删除接口");   // 1

        Long rank1 = redisService.rank(key, "dfg");
        log.info("测试ZSET类型根据分数从小到大排名接口，{}排名：{}","dfg",rank1);   // 2

        Long rank2 = redisService.reverseRank(key, "dfg");
        log.info("测试ZSET类型根据分数从大到小排名接口，{}排名：{}","dfg",rank2);   // 0

        redisService.zAdd(key, "bcd", 4);
        Set<Object> range = redisService.range(key, 0, 1);  // 结束节点为-1，即到最后一个元素
        log.info("测试ZSET类型获取区间内个数集合接口，{}",range);  // [asd, bcd]
        Set<Object> scoreRange = redisService.rangeByScore(key, 4, 7);
        log.info("测试ZSET类型获取分数区间内集合接口，{}",scoreRange);  // [asd, bcd, qwe]
    }

    @Test
    public void testBatchAddZset(){
        String key = "zset2";
        Set<String> set = new HashSet<>();
        set.add("qwe");
        set.add("sdf");
        set.add("fdv");
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();


        redisService.batchAddZset(key, tuples);
    }

    /****************   通用操作  ****************/
    /**
     * 设置key的过期时间
     */
    @Test
    public void testExpireKey(){
        String key = "asd";
        Long expireTime = 100L;
        redisService.expireKey(key, expireTime);
        log.info("成功设置过期时间");
    }
    /**
     * 判断键值key是否存在
     */
    @Test
    public void testKeyExists(){
        String key = "asd";
        Boolean exists = redisService.existsKey(key);
        if(exists){
            log.info("测试键是否存在接口，{}键存在",key);
        }else {
            log.info("测试键是否存在接口，{}键不存在",key);
        }
    }
    /**
     * 批量删除key
     */
    @Test
    public void testBatchDelete(){
        Set<String> keys = new HashSet<>();
        keys.add("abc");
        keys.add("asd");
        keys.add("ghfdj");
        Long delNum = redisService.batchDelete(keys);
        log.info("测试批量删除接口，成功删除{}个key键",delNum);       // 成功删除1个key键
    }
    /**************    测试了以上内容

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
        Object obj = redisService.hGet(key, hashKey);
        log.info("获取hash类型"+key+"的"+hashKey+"属性值，{}",obj.toString());
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
