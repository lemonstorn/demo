package com.weaver.web.token.impl;

import com.weaver.web.config.WeaverSecurityProperties;
import com.weaver.web.constants.TokenHolderTypeConstants;
import com.weaver.web.model.TokenUser;
import com.weaver.web.token.TokenUserHandler;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "system.login.use", havingValue = TokenHolderTypeConstants.REDIS)
public class TokenUserRedisHandler implements TokenUserHandler {

	private static final Logger logger = LoggerFactory.getLogger(TokenUserRedisHandler.class);

	@Autowired
	private WeaverSecurityProperties weaverSecurityProperties;
	@Autowired
	private RedisTemplate<String, Object> redisSessionTemplate;
	@Value("${spring.application.name:appname}")
	private String appName;

	@PostConstruct
	public void init() {
		logger.info("===>>>login with Redis");
	}

	@Override
	public <T> T getSessionUser(Class<T> clazz) {
		return getSessionUser(getRequest(), clazz);
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public <T> T getSessionUser(HttpServletRequest request, Class<T> clazz) {
		if (request == null) {
			request = getRequest();
		}
		String key = "SESSION_" + appName.toUpperCase() + "_" + request.getSession().getId();
		T t = (T) redisSessionTemplate.boundValueOps(key).get();
		if (t != null) {
			redisSessionTemplate.expire(key, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
		}
		return t;
	}

	@Override
	public Object getSessionUser() {
		String key = "SESSION_" + appName.toUpperCase() + "_" + getSession().getId();
		if (Boolean.TRUE.equals(redisSessionTemplate.hasKey(key))) {
			Object o = redisSessionTemplate.boundValueOps(key).get();
			if (o != null) {
				redisSessionTemplate.expire(key, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
			}
			return o;
		}
		return null;
	}

	@Override
	public String setSessionUser(String userid, TokenUser obj) {
		String token = getSession().getId();
		String key = "SESSION_" + appName.toUpperCase() + "_" + getSession().getId();
		String keyUserId = "SESSION_USER_ID_" + appName.toUpperCase() + "_" + getSession().getId();
		redisSessionTemplate.opsForValue().set(key, obj);
		redisSessionTemplate.expire(key, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
		redisSessionTemplate.opsForValue().set(keyUserId, userid);
		redisSessionTemplate.expire(keyUserId, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
		return token;
	}

	@Override
	public String refreshSessionUser() {
		return getSession().getId();
	}

	@Override
	public void removeSessionUser(String userId, String token) {
		String key = "SESSION_" + appName.toUpperCase() + "_" + token;
		redisSessionTemplate.delete(key);
	}

	@Override
	public void removeSessionUser() {
		String key = "SESSION_" + appName.toUpperCase() + "_" + getSession().getId();
		redisSessionTemplate.delete(key);
	}

	@Override
	public String getSessionUserId() {
		String key = "SESSION_USER_ID_" + appName.toUpperCase() + "_" + getSession().getId();
		if (redisSessionTemplate.hasKey(key)) {
			Object o = redisSessionTemplate.boundValueOps(key).get();
			if (o != null) {
				redisSessionTemplate.expire(key, weaverSecurityProperties.getTime(), TimeUnit.SECONDS);
			}
			return o.toString();
		}
		return null;
	}

    @Override
    public long getExpirationDate(String token) {
        String key = "SESSION_" + appName.toUpperCase() + "_" + token;
        Long time = redisSessionTemplate.getExpire(key);
        return null == time ? 0 : time / 1000;
    }

}
