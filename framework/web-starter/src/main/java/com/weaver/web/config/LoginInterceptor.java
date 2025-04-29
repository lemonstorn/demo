package com.weaver.web.config;

import com.weaver.core.model.WeaverResponse;
import com.weaver.web.annotation.NoLogin;
import com.weaver.web.constants.TokenUserSessionConstants;
import com.weaver.web.holder.WebContextHolder;
import com.weaver.web.token.TokenUserHandler;
import com.weaver.web.utils.WebContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private WeaverSecurityProperties weaverSecurityProperties;
	@Autowired
	private TokenUserHandler tokenUserHandler;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		WebContextHolder.setRequest(request);
		WebContextHolder.setResponse(response);
		String requestURI = request.getRequestURI().replace(request.getContextPath(), "");
		if (requestURI.equals(weaverSecurityProperties.getLoginUri())) {
			return true;
		}
		if (!weaverSecurityProperties.getNeed()) {
			return true;
		}
		boolean noLogin = noLoginCheck(handler);
		if (noLogin) {
			return true;
		}
		Object user = tokenUserHandler.getSessionUser();
		if (user != null) {
			if (user instanceof WeaverResponse) {
				WebContextUtils.responseOutWithJson(response, user);
				return false;
			}
			request.setAttribute(TokenUserSessionConstants.SESSION_INFO, user);
			return true;
		}
		if (WebContextUtils.isAjax(request)) {
			if (handler instanceof ResourceHttpRequestHandler) {
				WebContextUtils.responseOutWithJson(response, WeaverResponse.notFound());
			} else {
				WebContextUtils.responseOutWithJson(response, WeaverResponse.noLogin());
			}
        } else {
			if (handler instanceof ResourceHttpRequestHandler) {
				response.sendRedirect(WebContextUtils.getSystemPath(request) + weaverSecurityProperties.getNotFindUri());
			} else {
				String url = WebContextUtils.getSystemPath(request) + weaverSecurityProperties.getLoginUri();
				url += (url.contains("?") ? "&" : "?");
				url += "redirectUrl=" + request.getRequestURL();
				response.sendRedirect(url);
			}
        }
        return false;
    }

	/**
	 * 判定是否需要登录
	 * @param handler 请求方法
	 * @return 是否需要登录
	 */
	private static boolean noLoginCheck(Object handler) {
		boolean need = false;
		if (handler instanceof HandlerMethod handlerMethod) {
            NoLogin nologin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NoLogin.class);
			if (nologin != null) {
				need = nologin.isNeedLogin();
			}
			if (!need) {
				NoLogin login = handlerMethod.getMethod().getAnnotation(NoLogin.class);
				if (login != null) {
					need = login.isNeedLogin();
				}
			}
		}
		if (handler instanceof AbstractHandlerMapping) {
			need = true;
		}
		return need;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 请求完成后进行操作
     */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			WebContextHolder.removeRequest();
			WebContextHolder.removeResponse();
//			if (ex != null) {
//				logger.error("LoginInterceptor throws Exceptions ", ex);
//				if (WebContextUtils.isAjax(request)) {
//					WebContextUtils.responseOutWithJson(response, WeaverResponse.error(ex.getMessage()));
//				} else {
//					request.setAttribute("status", CodeEnum.Fail.getCode());
//					request.setAttribute("msg", ex.getMessage());
//				}
//			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
