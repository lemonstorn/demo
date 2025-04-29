//package com.weaver.security.config;
//
//import com.asiabdx.auth.utils.RedisUtils;
//import com.asiabdx.auth.web.WebContextHander;
//import com.asiabdx.core.exception.BootCustomException;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@Aspect
//@Configuration
//public class BdxDuplicateAutoConfiguration {
//
//	@Value("${spring.application.name:appname}")
//	private String appName;
//	@Resource
//	private WebContextHander webContextHander;
//
//	@Pointcut("@annotation(com.asiabdx.auth.annotation.Duplicate)")
//	public void duplicateSubmit() {
//	}
//
//	@Before("duplicateSubmit()")
//	public void before(final JoinPoint joinPoint) {
//		add(joinPoint);
//	}
//
//	@AfterReturning("duplicateSubmit()")
//	public void doAfterReturning(JoinPoint joinPoint) {
//		remove(joinPoint);
//	}
//
//	@AfterThrowing("duplicateSubmit()")
//	public void ex(JoinPoint joinPoint) {
//		remove(joinPoint);
//	}
//
//	private void add(JoinPoint joinPoint) {
//		String userId = webContextHander.getSessionUserId();
//		if (StringUtils.isBlank(userId)) {
//			userId = webContextHander.getSession().getId();
//		}
//		RedisTemplate<String, Object> redisTemplate = RedisUtils.getRedisTemplate();
//		if (redisTemplate == null) {
//			String TOKEN_KEY = String.format("%s.%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
//					joinPoint.getSignature().getName(), userId);
//			Object t = webContextHander.getSession().getAttribute(TOKEN_KEY);
//			if (null == t) {
//				String uuid = UUID.randomUUID().toString();
//				webContextHander.getSession().setAttribute(TOKEN_KEY, uuid);
//			} else {
//				throw new BootCustomException("请不要重复请求！");
//			}
//		} else {
//			String key = String.format("%s.%s.%s.%s.%s", "Duplicate", appName,
//					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), userId);
//			Object t = redisTemplate.boundValueOps(key).get();
//			if (null == t) {
//				redisTemplate.opsForValue().set(key, "1");
//				redisTemplate.expire(key, 60, TimeUnit.SECONDS);
//			} else {
//				throw new BootCustomException("请不要重复请求！");
//			}
//
//		}
//	}
//
//	private void remove(JoinPoint joinPoint) {
//		String userId = webContextHander.getSessionUserId();
//		if (StringUtils.isBlank(userId)) {
//			userId = webContextHander.getSession().getId();
//		}
//		RedisTemplate<String, Object> redisTemplate = RedisUtils.getRedisTemplate();
//		if (redisTemplate == null) {
//			String TOKEN_KEY = String.format("%s.%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
//					joinPoint.getSignature().getName(), userId);
//			Object t = webContextHander.getSession().getAttribute(TOKEN_KEY);
//			if (null != t) {// 方法执行完毕移除请求重复标记
//				webContextHander.getSession().removeAttribute(TOKEN_KEY);
//			}
//		} else {
//			String key = String.format("%s.%s.%s.%s.%s", "Duplicate", appName,
//					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), userId);
//			if (redisTemplate.hasKey(key)) {
//				redisTemplate.delete(key);
//			}
//		}
//	}
//}
