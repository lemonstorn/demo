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
@TableName("sys_menu_action_rel")
@Schema(name = "SysMenuActionRel", description = "")
public class SysMenuActionRel extends WeaverBaseEntity<SysMenuActionRel> {

    @TableField("menu_id")
    private Long menuId;

    @TableField("action_id")
    private Long actionId;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
