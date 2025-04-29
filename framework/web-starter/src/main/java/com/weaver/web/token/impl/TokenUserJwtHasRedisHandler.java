package com.weaver.web.token.impl;

import com.weaver.db.utils.RedisUtils;
import com.weaver.core.model.WeaverResponse;
import com.weaver.core.utils.JsonUtils;
import com.weaver.core.utils.StringUtils;
import com.weaver.web.config.WeaverSecurityProperties;
import com.weaver.web.constants.TokenHolderTypeConstants;
import com.weaver.web.model.TokenUser;
import com.weaver.web.utils.JwtTokenUtil;
import com.weaver.web.token.TokenUserHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 无状态jwt+redis实现
 * 用户登录时输出jwt，用户退出登录时，将用户信息存入redis，标记token已失效
 * 仅当需要进行单点登录时，才将token 存入redis
 */
@Component
@ConditionalOnClass(RedisTemplate.class)
@ConditionalOnBean(RedisTemplate.class)
@ConditionalOnProperty(name = "system.login.use", havingValue = TokenHolderTypeConstants.JWT_REDIS)
@Slf4j
public class TokenUserJwtHasRedisHandler implements TokenUserHandler {

	@Value("${spring.application.name:appname}")
	private String appName;
	@Resource
	private WeaverSecurityProperties weaverSecurityProperties;

	@PostConstruct
	public void init() {
		log.info("===>>> login with Redis-Jwt");
	}

	@Override
	public <T> T getSessionUser(Class<T> clazz) {
		return getSessionUser(getRequest(), clazz);
	}

    @Override
    public <T> T getSessionUser(HttpServletRequest request, Class<T> clazz) {
        String token = getTokenString(request);
        if (isVaildToken(token)) {
            return null;
        }
        String user = JwtTokenUtil.getUser(weaverSecurityProperties, getTokenString(request));
        if (user != null) {
            return JsonUtils.jsonToPojo(user, clazz);
        }
        return null;
    }

	@Override
	public Object getSessionUser() {
		String token = getTokenString(getRequest());
		if (isVaildToken(token)) {
			if (weaverSecurityProperties.getSingle()) {
				return WeaverResponse.singleLogin();
			} else {
				return null;
			}
		}
		String user = JwtTokenUtil.getUser(weaverSecurityProperties, token);
		if (user != null) {
			return JsonUtils.jsonToPojo(user, Map.class);
		}
		return null;
	}

	@Override
	public String setSessionUser(String userid, TokenUser obj) {
		String userKey = "SESSION_USER_" + appName.toUpperCase() + "_USER_" + userid;
		RedisTemplate<String, Object> redisSessionTemplate = RedisUtils.getRedisTemplate();
		if (redisSessionTemplate != null && Boolean.TRUE.equals(redisSessionTemplate.hasKey(userKey))) {
			String token = StringUtils.toString(redisSessionTemplate.boundValueOps(userKey).get());
			if (weaverSecurityProperties.getSingle()) {
				removeSessionUser(userid, token);
			}
		}
		String objId = JsonUtils.objectToJson(obj);
		String token = JwtTokenUtil.createToken(userid, objId, weaverSecurityProperties);
		HttpServletResponse response = getResponse();
		response.addHeader("token", token);
		if (redisSessionTemplate != null && weaverSecurityProperties.getSingle()) {
			redisSessionTemplate.opsForValue().set(userKey, token);
			redisSessionTemplate.expire(userKey, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
		}
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

	private boolean isVaildToken(String token) {
		try {
			if (StringUtils.isBlank(token)) {
				return false;
			}
			RedisTemplate<String, Object> redisSessionTemplate = RedisUtils.getRedisTemplate();
			if (redisSessionTemplate != null) {
				String key = "SESSION_TOKEN_" + appName.toUpperCase() + "_" + token;
				return Boolean.TRUE.equals(redisSessionTemplate.hasKey(key));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		return false;
	}

	@Override
    public long getExpirationDate(String token) {
        Date d1 = JwtTokenUtil.getExpirationDate(weaverSecurityProperties, token);
        return (d1.getTime() - System.currentTimeMillis()) / 1000;
    }

	private void removeToken(String token) {
		try {
			RedisTemplate<String, Object> redisSessionTemplate = RedisUtils.getRedisTemplate();
			if (redisSessionTemplate != null) {
				String key = "SESSION_TOKEN_" + appName.toUpperCase() + "_" + token;
				redisSessionTemplate.opsForValue().set(key, 1);
				redisSessionTemplate.expire(key, getExpirationDate(token), TimeUnit.SECONDS);
				JwtTokenUtil.setJwtExpiration(weaverSecurityProperties, token);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
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

}
