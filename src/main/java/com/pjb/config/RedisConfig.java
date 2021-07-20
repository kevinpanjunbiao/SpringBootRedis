package com.pjb.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;

/**
 * Redis配置类
 * @author pan_junbiao
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport
{
    @Autowired
    private RedisProperties redisProperties;

    /**
     * Jedis连接池
     */
    @Bean("jedis.pool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config)
    {
        return new JedisPool(config, redisProperties.getHost(), redisProperties.getPort());
    }

    /**
     * Jedis连接池配置信息
     */
    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig()
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        //连接池最大连接数（使用负值表示没有限制）
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxActive());

        //连接池最大阻塞等待时间（使用负值表示没有限制）
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWait().toMillis());

        //连接池中的最大空闲连接
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());

        //连接池中的最小空闲连接
        jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());

        return jedisPoolConfig;
    }

    /**
     * 缓存对象集合中，缓存是以key-value形式保存的,
     * 当不指定缓存的key时，SpringBoot会使用keyGenerator生成Key。
     */
    @Bean
    public KeyGenerator keyGenerator()
    {
        return new KeyGenerator()
        {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                //类名+方法名
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 缓存管理器
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory)
    {
        RedisCacheManager cacheManager = RedisCacheManager.create(connectionFactory);
        //设置缓存过期时间

        return cacheManager;
    }

    /**
     * 实例化RedisTemplate对象
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory)
    {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
