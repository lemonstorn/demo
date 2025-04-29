package com.weaver.web.token;

import com.weaver.web.holder.WebContextHolder;
import com.weaver.web.model.TokenUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface TokenUserHandler {

	<T> T getSessionUser(Class<T> clazz);

	<T> T getSessionUser(HttpServletRequest request, Class<T> clazz);

	Object getSessionUser();

	String getSessionUserId();

	String setSessionUser(String userid, TokenUser obj);

	String refreshSessionUser();

	void removeSessionUser(String userId, String token);

	void removeSessionUser();
    
    default String getTokenString() {
        return getTokenString(getRequest());
    };

	
	default String getTokenString(HttpServletRequest request) {
	    return null;
	};

    /**
     * 剩余失效时间（单位秒）
     * 
     * @param token
     * @return
     */
    default long getExpirationDate(String token) {
        return 0;
    }

	default HttpSession getSession() {
		return WebContextHolder.getSession();
	}

	default HttpServletResponse getResponse() {
		return WebContextHolder.getResponse();
	}

	default HttpServletRequest getRequest() {
		return WebContextHolder.getRequest();
	}
}
