package com.weaver.web.utils;

import com.weaver.core.utils.JsonUtils;
import com.weaver.core.utils.SpringUtils;
import com.weaver.web.model.TokenUser;
import com.weaver.web.token.TokenUserHandler;
import com.weaver.web.holder.WebContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WebContextUtils {

	private static final Logger logger = LoggerFactory.getLogger(WebContextUtils.class);

	/**
	 * 获取当前用户信息
	 * @param clazz 用户类
	 * @return 用户信息
	 */
	public static <T extends TokenUser> T getSessionUser(Class<T> clazz) {
		TokenUserHandler tokenUserHandler = SpringUtils.getBean(TokenUserHandler.class);
		return tokenUserHandler.getSessionUser(WebContextHolder.getRequest(), clazz);
	}

	/**
	 * 获取当前用户id
	 * @return 用户id
	 */
	public static String getSessionUserId() {
		TokenUserHandler webContextHandler = SpringUtils.getBean(TokenUserHandler.class);
        return webContextHandler.getSessionUserId();
    }

	/**
	 * 设置当前用户信息
	 * @param userid 用户id
	 * @param obj token中用户信息
	 * @return 当前用户id
	 */
	public static String setSessionUser(String userid, TokenUser obj) {
		TokenUserHandler webContextHandler = SpringUtils.getBean(TokenUserHandler.class);
        return webContextHandler.setSessionUser(userid, obj);
    }

	/**
	 * 刷新当前用户信息
	 * @return 当前用户id
	 */
	public static String refreshSessionUser() {
		TokenUserHandler webContextHandler = SpringUtils.getBean(TokenUserHandler.class);
        return webContextHandler.refreshSessionUser();
    }

	/**
	 * 将指定用户踢出登录
	 * @param userId 用户id
	 * @param token
	 */
	public static void removeSessionUser(String userId, String token) {
		TokenUserHandler webContextHandler = SpringUtils.getBean(TokenUserHandler.class);
		webContextHandler.removeSessionUser(userId, token);
    }

	/**
	 * 移除当前用户
	 */
	public static void removeSessionUser() {
		TokenUserHandler webContextHandler = SpringUtils.getBean(TokenUserHandler.class);
		webContextHandler.removeSessionUser();
    }


	public static HttpSession getSession() {
		return WebContextHolder.getSession();
	}

	public static HttpServletResponse getResponse() {
		return WebContextHolder.getResponse();
	}

	public static HttpServletRequest getRequest() {
		return WebContextHolder.getRequest();
	}

	public static void responseOutWithJson(HttpServletResponse response, Object object) throws IOException {
		OutputStream out = null;
		try {
			String responseJSONObject = JsonUtils.objectToJson(object);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-type", "application/json;charset=UTF-8");
			out = response.getOutputStream();
			out.write(responseJSONObject.getBytes(StandardCharsets.UTF_8));
			out.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("accept") != null && request.getHeader("accept").contains("application/json"))
				|| (request.getHeader("X-Requested-With") != null
						&& request.getHeader("X-Requested-With").contains("XMLHttpRequest"));
	}

	public static String getSystemPath(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String path = request.getContextPath();
		return url.substring(0, url.indexOf(path) + path.length());
	}
}
