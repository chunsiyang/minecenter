package com.minecenter.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.minecenter.exception.CustomException;
import com.minecenter.util.common.Base64ConvertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

/**
 * JAVA-JWT工具类
 *
 * @author yangchunsi
 * @date 2018/8/30 11:45
 */
@Component
@PropertySource("classpath:config.properties")
public class JwtUtil {

    /**
     * 过期时间改为从配置文件获取
     */
    private static String accessTokenExpireTime;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String encryptJWTKey;

    /**
     * 校验token是否正确
     *
     * @param token Token
     * @return boolean 是否正确
     * @author yangchunsi
     * @date 2018/8/31 9:05
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getSubject(token) + Base64ConvertUtil.decode(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CustomException("JWTToken认证解密出现UnsupportedEncodingException异常");
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token jwt token
     * @param claim claim
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的 iss(该JWT的签发者)
     *
     * @param token jwt token
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static String getIssuer(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getIssuer();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的 iat(在什么时候签发的token)
     *
     * @param token jwt token
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的 exp(token什么时候过期)
     *
     * @param token jwt token
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static Date getExpiresAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的 nbf(token在此时间之前不能被接收处理)
     *
     * @param token jwt token
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static Date getNotBefore(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getNotBefore();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的 sub(该JWT所面向的用户)
     *
     * @param token jwt token
     * @return java.lang.String
     * @author yangchunsi
     * @date 2018/9/7 16:54
     */
    public static String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getSubject();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 生成签名
     *
     * @param account          帐号
     * @param currentTimeMills 系统时间
     * @param issuer           签发者
     * @return java.lang.String 返回加密的Token
     * @author yangchunsi
     * @date 2018/8/31 9:07
     */
    public static String sign(String account, Long currentTimeMills, String issuer) {
        try {
            // 帐号加JWT私钥加密
            String secret = account + Base64ConvertUtil.decode(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(currentTimeMills + Long.parseLong(accessTokenExpireTime) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withSubject(account)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date(currentTimeMills))
                    .withExpiresAt(date)
                    .withNotBefore(new Date(currentTimeMills))
                    .withJWTId(UUID.randomUUID().toString().replace("-", "").toLowerCase())
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new CustomException("JWTToken加密出现UnsupportedEncodingException异常");
        }
    }

    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        JwtUtil.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Value("${encryptJWTKey}")
    public void setEncryptJWTKey(String encryptJWTKey) {
        JwtUtil.encryptJWTKey = encryptJWTKey;
    }
}
