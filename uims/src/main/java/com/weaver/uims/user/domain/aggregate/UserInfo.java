package com.weaver.uims.user.domain.aggregate;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weaver.ddd.aggregate.AggregateRoot;
import com.weaver.uims.model.po.SysAction;
import com.weaver.uims.model.po.SysMenu;
import com.weaver.uims.model.po.SysRole;
import com.weaver.uims.user.domain.model.SysUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: zh
 * @CreateTime: 2025-04-19
 * @Description: 用户信息聚合
 * @Version: 1.0
 */
@Accessors(chain = true,fluent = true)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo extends AggregateRoot {
    private String token;
    private SysUser user;
    private List<SysRole> roleList;
    private List<SysMenu> menuList;
    private List<SysAction> actionList;

    public void createUser() {
        //空实现，一般会在里面发个用户创建的事件
        logDebug("创建用户",user());
    }


    public static class UserInfoFactory {
        public static UserInfo getUser(String userName, String password) {
            SysUser sysUser = new SysUser()
                    .selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName,userName).eq(SysUser::getPassword,password));
            Assert.notNull(sysUser,"用户名或密码错误");
            UserInfo userInfo = new UserInfo()
                    .user(sysUser);
            return userInfo;
        }
        public static UserInfo getUser(Long userId) {
            SysUser sysUser = new SysUser().selectById(userId);
            Assert.notNull(sysUser,"当前用户不存在");
            UserInfo userInfo = new UserInfo()
                    .user(sysUser);
            return userInfo;
        }

        public static UserInfo createUserInfo(SysUser sysUser) {
            UserInfo userInfo = new UserInfo()
                    .user(sysUser);
            return userInfo;
        }
    }
}
