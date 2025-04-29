package com.weaver.uims.service;

import com.weaver.uims.user.command.dto.UserLoginDto;

/**
 * @author zh 2024/5/2 1:02
 */
public interface IUserService {

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     */
    void login(UserLoginDto userLoginDto);
}
