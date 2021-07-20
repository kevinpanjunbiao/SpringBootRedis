package com.pjb;

import com.pjb.common.RedisKeyEnum;
import com.pjb.utils.RedisUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

/**
 * Redis中Keys命令的使用
 * @author pan_junbiao
 **/
@SpringBootTest
public class KeysTest
{
    @BeforeEach
    void setUp()
    {
        System.out.println("\n\n\n");
    }

    @AfterEach
    void tearDown()
    {
        System.out.println("\n\n\n");
    }



    /**
     * Redis使用通配符进行查询
     * @author pan_junbiao
     */
    @Test
    public void queryTest()
    {
        try
        {
            //Reids中添加数据，键格式：USER::用户ID
            redisUtils.set("USER::1","pan_junbiao的博客_01");
            redisUtils.set("USER::2","pan_junbiao的博客_02");
            redisUtils.set("USER::3","pan_junbiao的博客_03");

            //Redis键：使用 * 号通配符
            String patternKey = "USER::*";

            //使用Keys命令获取匹配的key集合。
            Set<String> keySet = redisUtils.keys(patternKey);
            for(String key : keySet)
            {
                //从key键中提取用户ID
                String userId = key.replaceAll("USER::*", "");

                //Redis根据key键，查询对应的值
                String value = redisUtils.get(key);

                //打印信息
                System.out.println("Key键：" + key + " 用户ID：" + userId + " 用户名称：" + value);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    /**
     * Redis中字符串（String）的批量删除
     * @author pan_junbiao
     */
    @Test
    public void delStringTest()
    {
        try
        {
            //Redis键：使用 * 号通配符
            String patternKey = "USER::*";

            //Reids中添加数据，键格式：USER::用户ID
            redisUtils.set("USER::1","pan_junbiao的博客_01");
            redisUtils.set("USER::2","pan_junbiao的博客_02");
            redisUtils.set("USER::3","pan_junbiao的博客_03");

            //Reids批量删除字符串（String）
            long result = redisUtils.delAllByKey(patternKey);
            System.out.println("批量删除字符串（String）的结果：" + result);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Redis工具类
     */
    @Autowired
    private RedisUtils redisUtils;

    /**
     * Redis中哈希（Hash）的批量删除
     * @author pan_junbiao
     */
    @Test
    public void delHashTest()
    {
        try
        {
            //构建Redis键（格式：PJB_USER_INFO_::用户ID）
            String key1 = redisUtils.getRedisKey(RedisKeyEnum.USER_INFO,1); //键：PJB_USER_INFO_::1
            String key2 = redisUtils.getRedisKey(RedisKeyEnum.USER_INFO,2); //键：PJB_USER_INFO_::2
            String patternKey = redisUtils.getRedisKey(RedisKeyEnum.USER_INFO,"*"); //键：PJB_USER_INFO_::*

            //创建用户1：保存到Redis中，哈希（Hash）数据类型
            redisUtils.hset(key1,"userId",1);
            redisUtils.hset(key1,"userName","pan_junbiao的博客");
            redisUtils.hset(key1,"blogUrl","https://blog.csdn.net/pan_junbiao");
            redisUtils.hset(key1,"blogRemark","您好，欢迎访问 pan_junbiao的博客");

            //创建用户2：保存到Redis中，哈希（Hash）数据类型
            redisUtils.hset(key2,"userId",2);
            redisUtils.hset(key2,"userName","pan_junbiao的博客");
            redisUtils.hset(key2,"blogUrl","https://blog.csdn.net/pan_junbiao");
            redisUtils.hset(key2,"blogRemark","您好，欢迎访问 pan_junbiao的博客");

            //从Redis中读取数据，并打印
            System.out.println("用户ID：" + redisUtils.hget(key1,"userId"));
            System.out.println("用户名称：" + redisUtils.hget(key1,"userName"));
            System.out.println("博客地址：" + redisUtils.hget(key1,"blogUrl"));
            System.out.println("博客信息：" + redisUtils.hget(key1,"blogRemark"));

            System.out.println("\n用户ID：" + redisUtils.hget(key2,"userId"));
            System.out.println("用户名称：" + redisUtils.hget(key2,"userName"));
            System.out.println("博客地址：" + redisUtils.hget(key2,"blogUrl"));
            System.out.println("博客信息：" + redisUtils.hget(key2,"blogRemark"));

            //Reids批量删除哈希（Hash）
            long result = redisUtils.hdelAllByKey(patternKey);
            System.out.println("\n批量删除字符串（String）的结果：" + result);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
