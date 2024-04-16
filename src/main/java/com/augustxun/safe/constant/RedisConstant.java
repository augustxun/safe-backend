package com.augustxun.safe.constant;

public interface RedisConstant {
    public static final String LOGIN_CODE_KEY="login:code:";
    public static final long LOGIN_CODE_TTL=2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;
}
