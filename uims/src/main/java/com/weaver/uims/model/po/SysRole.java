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
@TableName("sys_role")
@Schema(name = "SysRole", description = "")
public class SysRole extends WeaverBaseEntity<SysRole> {

    @TableField("role_name")
    private String roleName;

    @TableField("remark")
    private String remark;

    @TableField("`source`")
    private String source;

    @TableField("`status`")
    private Integer status;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
