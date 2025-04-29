package com.weaver.uims.user;

import com.weaver.uims.user.command.UserCommandService;
import com.weaver.uims.user.command.dto.UserLoginDto;
import com.weaver.uims.user.domain.aggregate.UserInfo;
import com.weaver.uims.user.domain.model.SysUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zh
 * @CreateTime: 2025-04-12
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/user")
@AllArgsConstructor
@Validated
public class UserController {
    private final UserCommandService userCommandService;
    @PostMapping("/login")
    public UserInfo login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userCommandService.login(userLoginDto);
    }
    @PostMapping("/create")
    public void create(@RequestBody @Valid SysUser sysUser) {
        userCommandService.create(sysUser);
    }
    @PostMapping("/delete")
    public void delete(@RequestBody @Valid Long id) {
        userCommandService.delete(id);
    }
}
