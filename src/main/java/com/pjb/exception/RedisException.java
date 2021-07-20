package com.pjb.exception;

/**
 * Redis异常类
 * @author pan_junbiao
 **/
public class RedisException extends Exception
{
    public RedisException(String message, Throwable cause)
    {
        super("Redis服务异常：" + message, cause);
    }

    public RedisException(String message)
    {
        super("Redis服务异常：" + message);
    }

    public RedisException(Throwable cause)
    {
        super("Redis服务异常：", cause);
    }
}
