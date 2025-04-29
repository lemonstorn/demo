package com.weaver.web.token.impl;

import com.weaver.web.config.WeaverSecurityProperties;
import com.weaver.web.constants.TokenHolderTypeConstants;
import com.weaver.web.constants.TokenUserSessionConstants;
import com.weaver.web.model.TokenUser;
import com.weaver.web.token.TokenUserHandler;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "system.login.use", havingValue = TokenHolderTypeConstants.SESSION, matchIfMissing = true)
public class TokenUserSessionHandler implements TokenUserHandler {

	private static final Logger logger = LoggerFactory.getLogger(TokenUserSessionHandler.class);

	@Autowired
	public WeaverSecurityProperties weaverSecurityProperties;

	@PostConstruct
	public void init() {
		logger.info("===>>>login with Session");
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public <T> T getSessionUser(Class<T> clazz) {
		return (T) getSession().getAttribute(TokenUserSessionConstants.SESSION_INFO);
	}

	@Override
	public Object getSessionUser() {
		return getSession().getAttribute(TokenUserSessionConstants.SESSION_INFO);
	}

	@Override
	public String setSessionUser(String userid, TokenUser obj) {
		getSession().setAttribute(TokenUserSessionConstants.SESSION_INFO_USER_ID, userid);
		getSession().setAttribute(TokenUserSessionConstants.SESSION_INFO, obj);
		return getSession().getId();
	}

	@Override
	public String refreshSessionUser() {
		return getSession().getId();
	}

	@Override
	public void removeSessionUser(String userId, String token) {
		getSession().removeAttribute(TokenUserSessionConstants.SESSION_INFO);

	}

	@Override
	public void removeSessionUser() {
		getSession().removeAttribute(TokenUserSessionConstants.SESSION_INFO);
	}

	@Override
	public String getSessionUserId() {
		return (String) getSession().getAttribute(TokenUserSessionConstants.SESSION_INFO_USER_ID);
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public <T> T getSessionUser(HttpServletRequest request, Class<T> clazz) {
		return (T) getRequest().getSession().getAttribute(TokenUserSessionConstants.SESSION_INFO);
	}

}
