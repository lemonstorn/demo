package com.weaver.web.token.impl;

import com.weaver.core.utils.JsonUtils;
import com.weaver.core.utils.StringUtils;
import com.weaver.web.config.WeaverSecurityProperties;
import com.weaver.web.constants.TokenHolderTypeConstants;
import com.weaver.web.model.TokenUser;
import com.weaver.web.utils.JwtTokenUtil;
import com.weaver.web.token.TokenUserHandler;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@ConditionalOnMissingClass("org.springframework.data.redis.core.RedisTemplate")
@ConditionalOnProperty(name = "system.login.use", havingValue = TokenHolderTypeConstants.JWT_SESSION)
public class TokenUserJwtNoRedisHandler implements TokenUserHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenUserJwtNoRedisHandler.class);

    @Value("${spring.application.name:appname}")
    private String appName;
    @Autowired
    private WeaverSecurityProperties weaverSecurityProperties;

    @PostConstruct
    public void init() {
        logger.info("===>>>login with Jwt");
    }

    @Override
    public <T> T getSessionUser(Class<T> clazz) {
        return getSessionUser(getRequest(), clazz);
    }

    @Override
    public <T> T getSessionUser(HttpServletRequest request, Class<T> clazz) {
        String user = JwtTokenUtil.getUser(weaverSecurityProperties, getTokenString(request));
        if (user != null) {
            return JsonUtils.jsonToPojo(user, clazz);
        }
        return null;
    }

    @Override
    public Object getSessionUser() {
        String token = getTokenString(getRequest());
        String user = JwtTokenUtil.getUser(weaverSecurityProperties, token);
        if (user != null) {
            return JsonUtils.jsonToPojo(user, Map.class);
        }
        return null;
    }

    @Override
    public String setSessionUser(String userid, TokenUser obj) {
        String token;
        String objId = JsonUtils.objectToJson(obj);
        token = JwtTokenUtil.createToken(userid, objId, weaverSecurityProperties);
        HttpServletResponse response = getResponse();
        response.addHeader("token", token);
        return token;
    }

    @Override
    public String refreshSessionUser() {
        String tokenHeader = getTokenString(getRequest());
        removeToken(tokenHeader);
        String id = JwtTokenUtil.getUserId(weaverSecurityProperties, tokenHeader);
        String user = JwtTokenUtil.getUser(weaverSecurityProperties, tokenHeader);
		String token = JwtTokenUtil.createToken(id, user, weaverSecurityProperties);
        JwtTokenUtil.setJwtExpiration(weaverSecurityProperties, tokenHeader);
        return token;
    }

    @Override
    public void removeSessionUser(String userId, String token) {
        removeToken(token);
    }

    @Override
    public void removeSessionUser() {
        removeToken(getTokenString(getRequest()));
    }

    private void removeToken(String token) {
        JwtTokenUtil.setJwtExpiration(weaverSecurityProperties, token);
    }

    @Override
    public String getTokenString(HttpServletRequest request) {
        if (request == null) {
            request = getRequest();
        }
        String tokenHeader = request.getHeader(weaverSecurityProperties.getTokenHeader());
        if (StringUtils.isBlank(tokenHeader)) {
            return null;
        }
        if (!tokenHeader.startsWith(weaverSecurityProperties.getTokenPrefix())) {
            return null;
        }
        return tokenHeader.replace(weaverSecurityProperties.getTokenPrefix(), "");
    }

    @Override
    public String getSessionUserId() {
        return JwtTokenUtil.getUserId(weaverSecurityProperties, getTokenString(getRequest()));
    }

    @Override
    public long getExpirationDate(String token) {
        Date d1 = JwtTokenUtil.getExpirationDate(weaverSecurityProperties, token);
        return (d1.getTime() - System.currentTimeMillis()) / 1000;
    }
}
