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
@TableName("sys_user_menu_rel")
@Schema(name = "SysUserMenuRel", description = "")
public class SysUserMenuRel extends WeaverBaseEntity<SysUserMenuRel> {

    @TableField("user_id")
    private Long userId;

    @TableField("menu_id")
    private Long menuId;

    @TableField("action_id")
    private Long actionId;

    @TableField("`source`")
    private Integer source;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
