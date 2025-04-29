package com.weaver.web.utils;

import com.weaver.core.utils.StringUtils;
import com.weaver.web.config.WeaverSecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Date;

import static com.weaver.core.constants.Sym.RSA;

@Slf4j
public class JwtTokenUtil {

    private static final String ISS = "leopard";

    public static String createToken(String id, String user, WeaverSecurityProperties weaverSecurityProperties) {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtSignWith = weaverSecurityProperties.getJwtSignWith();
        if (jwtSignWith == null || !jwtSignWith.equals(RSA)) {
            String jwtSecret = weaverSecurityProperties.getJwtSecret();
            SecretKey key = getKey(jwtSecret);
            jwtBuilder.signWith(key);
        } else {
            jwtBuilder.signWith(weaverSecurityProperties.getPrivateKey(), Jwts.SIG.RS512);
        }
        jwtBuilder.subject(user);
        jwtBuilder.issuer(ISS);
        jwtBuilder.id(id);
        jwtBuilder.issuedAt(new Date());
        jwtBuilder.expiration(new Date(System.currentTimeMillis() + weaverSecurityProperties.getTime() * 1000));
        return jwtBuilder.compact();
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUser(WeaverSecurityProperties bdxAuthProperties, String token) {
        Claims claims = getTokenBody(bdxAuthProperties, token);
        if (claims != null) {
            return claims.getSubject();
        } else {
            return null;
        }
    }

    /**
     * 从token中获取ID，同时做解密处理
     *
     * @param token
     * @return
     */
    public static String getUserId(WeaverSecurityProperties bdxAuthProperties, String token) {
        Claims claims = getTokenBody(bdxAuthProperties, token);
        if (claims != null) {
            return claims.getId();
        } else {
            return null;
        }
    }

    /**
     * 获取失效时间
     *
     * @param token
     * @return
     * @throws 过期无法判断，只能通过捕获ExpiredJwtException异常
     */
    public static Date getExpirationDate(WeaverSecurityProperties bdxAuthProperties, String token) {
        return getTokenBody(bdxAuthProperties, token).getExpiration();
    }

    /**
     * 设置失效
     *
     * @param token
     * @return
     * @throws 自定义UserLoginException异常处理
     */
    public static void setJwtExpiration(WeaverSecurityProperties bdxAuthProperties, String token) {
        Claims claims = getTokenBody(bdxAuthProperties, token);
        if (claims != null) {
            claims.clear();
        }
    }

    /**
     * 获取token信息，同时也做校验处理
     *
     * @param token
     * @return
     * @throws 自定义UserLoginException异常处理
     */
    public static Claims getTokenBody(WeaverSecurityProperties bdxAuthProperties, String token) {
        try {
            if (StringUtils.isBlank(token)) {
                return null;
            }
            String jwtSignWith = bdxAuthProperties.getJwtSignWith();
            if (jwtSignWith == null || !jwtSignWith.equals("RSA")) {
                SecretKey key = getKey(bdxAuthProperties.getJwtSecret());
                return getTokenBody(key, token);
            } else {
                return getTokenBody(bdxAuthProperties.getPublicKey(), token);
            }
        } catch (ExpiredJwtException expired) {
            log.error("token[{}]已过期", token);
            return null;
        } catch (MalformedJwtException malformedJwt) {
            log.error("token[{}]无效", token);
            return null;
        }
    }

    public static Claims getTokenBody(SecretKey key, String token) {
        return parseToken(token, Jwts.parser().verifyWith(key));
    }


    public static Claims getTokenBody(PublicKey key, String token) {
        return parseToken(token, Jwts.parser().verifyWith(key));
    }

    @Nullable
    private static Claims parseToken(String token, JwtParserBuilder jwtParserBuilder) {
        try {
            if (StringUtils.isBlank(token)) {
                return null;
            }
            return jwtParserBuilder.build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException expired) {
            log.error("token[{}]已过期", token);
            return null;
        } catch (MalformedJwtException e) {
            log.error("token[{}]无效", token);
            return null;
        }
    }

    private static SecretKey getKey(String content){
        byte[] bytes = Decoders.BASE64.decode(content);
        return Keys.hmacShaKeyFor(bytes);
    }

}
