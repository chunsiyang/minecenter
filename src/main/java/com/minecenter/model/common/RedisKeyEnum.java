package com.minecenter.model.common;

public enum RedisKeyEnum {
    /**
     * redis-key-前缀-shiro:cache:authorizationcache
     */
    PREFIX_SHIRO_CACHE_AUTHORIZATIONCACHE("shiro:cache:authorizationCache:"),
    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    PREFIX_SHIRO_REFRESH_TOKEN("shiro:refresh_token:"),
    /**
     * redis-key-前缀-shiro:cache:authenticationCache
     */
    PREFIX_SHIRO_CACHE_AUTHENTICATIONCACHE("shiro:cache:authenticationCache:");

    private String key ;

    RedisKeyEnum(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
