/**
 * 
 */
package com.weaver.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author t-xiabin
 *
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext ctx;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.ctx = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	/**
	 * 获取对象   
	 * @param name
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return ctx.getBean(name);
	}
	
	/**
	 * 通过class获取对象
	 * @param clazz
	 * @return
	 */
    public static <T> T getBean(Class<T> clazz){
        return ctx.getBean(clazz);
    }

	/**
	 * 获取类型为requiredType的对象
	 * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	 * @param name       bean注册名
	 * @param requiredType 返回对象类型
	 * @return Object 返回requiredType类型对象
	 * @throws BeansException
	 */
	public static <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {
		return ctx.getBean(name, requiredType);
	}
}
