package com.weaver.uims.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weaver.db.model.WeaverBaseEntity;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zh
 * @since 2025-04-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role_rel")
@Schema(name = "SysUserRoleRel", description = "")
public class SysUserRoleRel extends WeaverBaseEntity<SysUserRoleRel> {

    @TableField("user_id")
    private String userId;

    @TableField("role_id")
    private String roleId;

    @TableField("`source`")
    private Integer source;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
