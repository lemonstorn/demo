package com.weaver.db.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: zh
 * @CreateTime: 2025-04-12
 * @Description: 缓存配置类
 * @Version: 1.0
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisTemplate.class)
public class CacheConfig implements CachingConfigurer {

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }
}
