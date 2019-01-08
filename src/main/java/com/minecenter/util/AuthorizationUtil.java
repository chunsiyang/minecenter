package com.minecenter.util;

import org.springframework.util.StringUtils;

public class AuthorizationUtil {

    /**
     *  Bearer Token 在 Authorization 中的名称定义
     */
    private static final String BEARER ="Bearer";

    /**
     * 根据JWT token 生成 Bearer Token
     *
     * @param  jwtToken JWT Token
     * @return String Bearer Token
     * @author : chunsiyang
     * @date : 2019年01月08日 上午 08:50:16
     */
    public static String getBearerToken(String jwtToken){
        String bearerToken="";
        if (StringUtils.hasText(jwtToken)) {
            bearerToken = "Bearer " + jwtToken;
        }
        return bearerToken;
    }

    /**
     *  根据请求BearerToken 获取JWT Token
     * @param  bearerToken Bearer Token
     * @return String JWT Token
     * @author : chunsiyang
     * @date : 2019年01月08日 上午 09:02:27
     */
    public static String getJWTToken(String bearerToken){
        String jwtToken =null;
        if (StringUtils.hasText(bearerToken)
                &&bearerToken.contains(AuthorizationUtil.BEARER)) {
            jwtToken = bearerToken.substring(7, bearerToken.length());
        }
        return jwtToken;
    }
}
