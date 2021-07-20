package com.pjb.utils;

import com.pjb.common.RedisKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.pjb.exception.RedisException;

import java.util.*;

/**
 * Redis工具类
 * @author pan_junbiao
 **/
@Component
public class RedisUtils
{
    @Autowired
    private JedisPool jedisPool;

    //Redis项目键
    @Value("${myenvironment.redis-project-key}")
    private String redisProjectKey;

    public String getRedisProjectKey()
    {
        return redisProjectKey;
    }

    public void setRedisProjectKey(String redisProjectKey)
    {
        this.redisProjectKey = redisProjectKey;
    }

    /**
     * 获取Jedis对象
     * @return
     */
    public Jedis getJedis()
    {
        return jedisPool.getResource();
    }

    /**
     * 释放资源
     * @param jedis Jedis对象
     */
    public void closeResource(Jedis jedis)
    {
        if (jedisPool != null && jedis != null)
        {
            //自Jedis3.0版本后jedisPool.returnResource()遭弃用，
            //官方重写了Jedis的close方法用以代替；
            jedis.close();
        }
    }

    /**
     * 获取Redis键
     */
    public String getRedisKey(RedisKeyEnum businessKey)
    {
        String key = this.redisProjectKey + "_" + businessKey;
        return key;
    }

    /**
     * 获取Redis键
     */
    public String getRedisKey(RedisKeyEnum businessKey, Object id)
    {
        String key = String.format("%s_%s::%s",this.redisProjectKey,businessKey,id.toString());
        return key;
    }

    /************************ 字符串（String） *************************/

    /**
     * 字符串（String）
     * 设置字符串值
     * @param key 键
     * @param value 值
     * @return 返回 OK 表示执行成功
     */
    public String set(String key, Object value) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            String valueStr = "";
            if (value != null)
            {
                valueStr = value.toString();
            }

            jedis = jedisPool.getResource();
            result = jedis.set(key, valueStr);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 获取字符串值
     * @param key 键
     * @return 字符串值
     */
    public String get(String key) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 获取键集合
     * @param key 键
     * @return 键集合
     */
    public Set<String> keys(String key) throws RedisException
    {
        Jedis jedis = null;
        Set<String> result = null;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.keys(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 删除字符串键
     * @param key 键
     * @return true：删除成功；false：删除失败
     */
    public boolean del(String key) throws RedisException
    {
        Jedis jedis = null;
        boolean result = false;
        try
        {
            jedis = jedisPool.getResource();
            jedis.del(key);
            result = true;
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 批量删除所有字符串键
     * 支持通配符*号等
     * @param key 键
     * @return 成功删除字段的数量
     * @author pan_junbiao
     */
    public Long delAllByKey(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            Set<String> keySet = jedis.keys(key);
            if (keySet != null && keySet.size() > 0)
            {
                String[] keyArray = keySet.toArray(new String[0]);
                result = jedis.del(keyArray);
            }
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 返回键所存储的字符串的长度
     * @param key 键
     * @return 字符串的长度
     */
    public Long getStrLen(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.strlen(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 判断键是否存在
     * @param key 键
     * @return true：键存在；false：键不存在
     */
    public Boolean exists(String key) throws RedisException
    {
        Jedis jedis = null;
        boolean result = false;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.exists(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 查看键的类型
     * @param key 键
     * @return 键的类型（如：string）
     */
    public String type(String key) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.type(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 将键重命名
     * @param oldkey 旧键名
     * @param newkey 新键名
     * @return 返回 OK 表示执行成功
     */
    public String rename(String oldkey, String newkey) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.rename(oldkey, newkey);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 自增1
     * @param key 键
     * @return 自增后的值
     */
    public Long incr(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.incr(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 根据增量数，自增
     * @param key 键
     * @param increment 增量数
     * @return 增量后的值
     */
    public Long incrBy(String key, long increment) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.incrBy(key, increment);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 递减1
     * @param key 键
     * @return 递减后的值
     */
    public Long decr(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.decr(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 根据递减量，递减
     * @param key 键
     * @param decrement 递减量
     * @return 递减后的值
     */
    public Long decrBy(String key, long decrement) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.decrBy(key, decrement);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 字符串（String）
     * 在键的值的后面拼接字符串
     * @param key 键
     * @param value 拼接字符串
     * @return 拼接字符串后的长度
     */
    public Long append(String key, Object value) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            String valueStr = "";
            if (value != null)
            {
                valueStr = value.toString();
            }

            jedis = jedisPool.getResource();
            result = jedis.append(key, valueStr);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /************************ 哈希（Hash） *************************/

    /**
     * 哈希（Hash）操作：
     * 将哈希表 key 中的字段 field 的值设为 value
     * @param key 键
     * @param field 域
     * @param value 值
     * @return 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。
     * 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hset(String key, String field, Object value) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            String valueStr = "";
            if (value != null)
            {
                valueStr = value.toString();
            }

            jedis = jedisPool.getResource();
            result = jedis.hset(key, field, valueStr);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 获取存储在哈希表中指定字段的值
     * @param key 键
     * @param field 域
     * @return 返回给定字段的值。如果给定的字段或 key 不存在时，返回 null 。
     * @throws RedisException
     */
    public String hget(String key, String field) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     * @param key 键
     * @param field 域
     * @param increment 增量值
     * @return 执行 HINCRBY 命令之后，哈希表中字段的值。
     */
    public Long hincrBy(String key, String field, long increment) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hincrBy(key, field, increment);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 获取在哈希表中指定 key 的所有字段和值
     * @param key 键
     * @return 返回哈希表中，所有的字段和值
     */
    public Map<String, String> hgetAll(String key) throws RedisException
    {
        Jedis jedis = null;
        Map<String, String> result = null;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 删除一个或多个哈希表字段
     * @param key 键
     * @param fields 域
     * @return 成功删除字段的数量
     */
    public Long hdel(String key, String... fields) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hdel(key,fields);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 批量删除所有哈希表字段
     * 支持通配符*号等
     * @param key 键
     * @return 成功删除字段的数量
     * @author pan_junbiao
     */
    public Long hdelAllByKey(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            Set<String> keySet = jedis.keys(key);
            if (keySet != null && keySet.size() > 0)
            {
                for (String itemKey : keySet)
                {
                    Map<String, String> fieldMap = jedis.hgetAll(itemKey);
                    if (fieldMap != null && fieldMap.size() > 0)
                    {
                        String[] fieldArray = fieldMap.keySet().toArray(new String[0]);
                        result += jedis.hdel(itemKey, fieldArray);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }


    /**
     * 哈希（Hash）操作：
     * 获取哈希表中字段的数量，当key不存在时返回 0
     * @param key 键
     * @return 哈希表中字段的数量
     */
    public Long hlen(String key) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hlen(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 哈希（Hash）操作：
     * 获取哈希表中的所有字段名
     * @param key 键
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    public Set<String> hkeys(String key) throws RedisException
    {
        Jedis jedis = null;
        Set<String> result = null;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /************************ 集合（Set） *************************/

    /**
     * 集合（Set）操作：
     * 将一个或多个成员元素加入到集合中，
     * 已经存在于集合的成员元素将被忽略
     * @param key 键
     * @param values 值
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    public Long sadd(String key, Object... values) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            List<String> stringList = new ArrayList<>();
            String str = "";
            for(Object obj : values)
            {
                if(obj!=null)
                {
                    str = obj.toString();
                }
                stringList.add(str);
            }

            String[] stringArray = stringList.toArray(new String[0]);

            jedis = jedisPool.getResource();
            result = jedis.sadd(key,stringArray);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 集合（Set）操作：
     * 随机返回集合中一个元素
     * @param key 键
     * @return 随机返回集合中一个元素
     */
    public String srandmember(String key) throws RedisException
    {
        Jedis jedis = null;
        String result = "";
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.srandmember(key);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /************************ 有序集合（Sorted Set） *************************/

    /**
     * 有序集合（Sorted Set）
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * @param key 键
     * @param score 分数
     * @param value 值
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    public Long zadd(String key, double score, Object value) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            String valueStr = "";
            if (value != null)
            {
                valueStr = value.toString();
            }

            jedis = jedisPool.getResource();
            result = jedis.zadd(key, score, valueStr);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 有序集合（Sorted Set）
     * 通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列。
     * @param key 键
     * @param start 开始索引
     * @param end 结束索引
     * @return 有序集成员按分数值递增(从小到大)顺序排列
     */
    public Set<String> zrange(String key, long start, long end) throws RedisException
    {
        Jedis jedis = null;
        Set<String> result = null;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.zrange(key, start, end);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 有序集合（Sorted Set）
     * 通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从大到小)顺序排列。
     * @param key 键
     * @param start 开始索引
     * @param end 结束索引
     * @return 有序集成员按分数值递增(从大到小)顺序排列
     */
    public Set<String> zrevrange(String key, long start, long end) throws RedisException
    {
        Jedis jedis = null;
        Set<String> result = null;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.zrevrange(key, start, end);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }

    /**
     * 有序集合（Sorted Set）
     * 移除有序集合中给定的排名区间的所有成员
     * @param key 键
     * @param start 开始索引
     * @param stop 结束索引
     * @return 被移除成员的数量
     */
    public Long zremrangeByRank(String key, long start, long stop) throws RedisException
    {
        Jedis jedis = null;
        long result = 0;
        try
        {
            jedis = jedisPool.getResource();
            result = jedis.zremrangeByRank(key, start, stop);
        }
        catch (Exception ex)
        {
            throw new RedisException(ex);
        }
        finally
        {
            //释放资源
            closeResource(jedis);
        }
        return result;
    }
}
