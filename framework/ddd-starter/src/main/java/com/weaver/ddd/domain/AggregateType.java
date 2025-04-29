package com.weaver.ddd.domain;

import lombok.Getter;

/**
 * 聚合根类型
 */
@Getter
public enum AggregateType {
    User("用户", "User"),
    Role("角色", "Role"),
    Permission("权限", "Permission"),
//    Tenant("租户", "Tenant"),
    Dept("部门", "Dept"),
    Menu("菜单", "Menu"),
    Dict("字典", "Dict"),
    ;

    private final String name;
    private final String type;
    AggregateType(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
