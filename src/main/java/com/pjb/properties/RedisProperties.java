package com.pjb.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis属性类
 * @author pan_junbiao
 **/
@Component
public class RedisProperties
{
    /**
     * Redis数据库索引
     */
    @Value("${spring.redis.database}")
    private int database;

    /**
     * Redis服务器地址
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * Redis服务器连接端口
     */
    @Value("${spring.redis.port}")
    private int port;

    /**
     * Redis服务器连接密码
     */
    @Value("${spring.redis.port}")
    private String password;

    /**
     * 连接池最大连接数
     */
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    /**
     * 连接池最大连接数
     */
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Duration maxWait;

    /**
     * 连接池中的最大空闲连接
     */
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    /**
     * 连接池中的最小空闲连接
     */
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    public int getDatabase()
    {
        return database;
    }

    public void setDatabase(int database)
    {
        this.database = database;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getMaxActive()
    {
        return maxActive;
    }

    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }

    public Duration getMaxWait()
    {
        return maxWait;
    }

    public void setMaxWait(Duration maxWait)
    {
        this.maxWait = maxWait;
    }

    public int getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle()
    {
        return minIdle;
    }

    public void setMinIdle(int minIdle)
    {
        this.minIdle = minIdle;
    }
}
