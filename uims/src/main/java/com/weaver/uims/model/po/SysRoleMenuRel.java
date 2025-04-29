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
@TableName("sys_role_menu_rel")
@Schema(name = "SysRoleMenuRel", description = "")
public class SysRoleMenuRel extends WeaverBaseEntity<SysRoleMenuRel> {

    @TableField("role_id")
    private String roleId;

    @TableField("menu_id")
    private String menuId;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
