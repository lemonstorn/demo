package com.weaver.web.model;

import lombok.Data;

import java.util.Map;

/**
 * @Author: zh
 * @CreateTime: 2025-04-12
 * @Description: token用户信息
 * @Version: 1.0
 */

@Data
public class TokenUser {
    //用户id
    private String id;
    //角色id
    private String roleId;
    //部门id
    private String deptId;
    //拓展信息
    private Map<String,Object> metaInfo;
}
