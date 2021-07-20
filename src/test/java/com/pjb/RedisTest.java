package com.pjb;

import com.pjb.common.RedisKeyEnum;
import com.pjb.exception.RedisException;
import com.pjb.utils.RedisUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Redis测试类
 * @author pan_junbiao
 **/
@SpringBootTest
public class RedisTest
{
    @BeforeEach
    void setUp()
    {
        System.out.println("\n\n");
    }

    @AfterEach
    void tearDown()
    {
        System.out.println("\n\n");
    }

    /**
     * 使用Redis的哈希（Hash）数据类型，保存用户信息
     * @author pan_junbiao
     */
    @Test
    public void addUserTest() throws RedisException
    {
        //构建Redis键（格式：PJB_USER_INFO_::用户ID）
        String key = redisUtils.getRedisKey(RedisKeyEnum.USER_INFO,1);

        //保存到Redis中，使用哈希（Hash）数据类型
        redisUtils.hset(key,"userId",1);
        redisUtils.hset(key,"userName","pan_junbiao的博客");
        redisUtils.hset(key,"blogUrl","https://blog.csdn.net/pan_junbiao");
        redisUtils.hset(key,"blogRemark","您好，欢迎访问 pan_junbiao的博客");

        //从Redis中读取数据，并打印
        System.out.println("用户ID：" + redisUtils.hget(key,"userId"));
        System.out.println("用户名称：" + redisUtils.hget(key,"userName"));
        System.out.println("博客地址：" + redisUtils.hget(key,"blogUrl"));
        System.out.println("博客信息：" + redisUtils.hget(key,"blogRemark"));
    }

    /**
     * Redis工具类
     */
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 批量删除Redis的哈希（Hash）数据类型
     * @author pan_junbiao
     */
    @Test
    public void delUserTest() throws RedisException
    {
        //构建Redis键（格式：PJB_USER_INFO_::用户ID）
        String key = redisUtils.getRedisKey(RedisKeyEnum.USER_INFO,1);

        //批量删除所有哈希表字段
        long result = redisUtils.delAllByKey(key);
        System.out.println("Redis删除结果：" + result);
    }
}
