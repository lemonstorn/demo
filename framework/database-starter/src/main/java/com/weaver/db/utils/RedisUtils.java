package com.weaver.db.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.weaver.core.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;

import java.util.List;

@Slf4j
public class RedisUtils {

	public static Boolean isRedisConnect = null;

	public static RedisTemplate<String, Object> getRedisTemplate() {
		if (isRedisConnect == null) {
			initRedisConnect();
		}
		if (!isRedisConnect) {
			return null;
		}
		return getRedisTemplateByBeanFactory();
	}

	@SuppressWarnings("unchecked")
	private static RedisTemplate<String, Object> getRedisTemplateByBeanFactory() {
        return (RedisTemplate<String, Object>) SpringUtils.getBean("redisTemplate");
	}

	private static void initRedisConnect() {
		try {
			RedisTemplate<String, Object> redisSessionTemplate = getRedisTemplateByBeanFactory();
			List<RedisClientInfo> list = redisSessionTemplate.getClientList();
			isRedisConnect = CollectionUtil.isNotEmpty(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			isRedisConnect = false;
		}
	}
}
