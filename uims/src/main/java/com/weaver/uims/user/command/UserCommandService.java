package com.weaver.uims.user.command;

import com.weaver.core.utils.EncryptUtils;
import com.weaver.uims.user.command.dto.UserLoginDto;
import com.weaver.uims.user.domain.aggregate.UserInfo;
import com.weaver.uims.user.domain.model.SysUser;
import com.weaver.web.utils.WebContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: zh
 * @CreateTime: 2025-04-19
 * @Description: 用户处理服务
 * @Version: 1.0
 */
@Component
@Slf4j
public class UserCommandService {
    @Value("${weaver.security.sm4.key:weaver2025_onDev}")
    private String sm4Key;

    public UserInfo login(UserLoginDto userLoginDto) {
        //解密密码
        String password = EncryptUtils.decryptSm4(userLoginDto.getPassword(), sm4Key);
        UserInfo userInfo = UserInfo.UserInfoFactory.getUser(userLoginDto.getUserName(), password);
        //自定义的一个token用户对象
        UimsTokenUser uimsTokenUser = new UimsTokenUser();
        String token = WebContextUtils.setSessionUser(userInfo.user().getIdString(), uimsTokenUser);
        userInfo.token(token);
        return userInfo;
    }

    public void create(SysUser sysUser) {
        UserInfo userInfo = UserInfo.UserInfoFactory.createUserInfo(sysUser);
        userInfo.createUser();
        userInfo.user().insert();
    }

    public UserInfo delete(Long id) {

        return null;
    }
}
