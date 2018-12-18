package com.minecenter.model.common;

public enum RedisKeyEnum {
    /**
     * redis-key-前缀-shiro:cache:
     */
    PREFIX_SHIRO_CACHE("shiro:cache:"),
    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    PREFIX_SHIRO_REFRESH_TOKEN("shiro:refresh_token:");

    private String key ;

    RedisKeyEnum(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
