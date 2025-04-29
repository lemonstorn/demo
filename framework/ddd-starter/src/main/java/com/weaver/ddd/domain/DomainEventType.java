package com.weaver.ddd.domain;

import lombok.Getter;

import static com.weaver.ddd.domain.AggregateType.*;

@Getter
public enum DomainEventType {
    USER_LOGIN("用户登录", User),
    USER_LOGOUT("用户登出",User),
    USER_REGISTER("用户注册",User),
    USER_CHANGE_PASSWORD("用户修改密码",User),

    ;

    DomainEventType(String name ,AggregateType aggregateType) {
        this.name = name;
        this.aggregateType = aggregateType;
    }

    private final AggregateType aggregateType;
    private final String name;
}
