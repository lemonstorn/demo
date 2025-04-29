package com.weaver.uims.user.command.dto;

import lombok.Data;

/**
 * @Author: zh
 * @CreateTime: 2025-04-13
 * @Description: 用户登录入参
 * @Version: 1.0
 */
@Data
public class UserLoginDto {
    private String userName;
    private String password;
    private String code;
}
