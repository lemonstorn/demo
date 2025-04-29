package com.weaver.uims.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weaver.db.model.WeaverBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("sys_action")
@Schema(name = "SysAction", description = "")
public class SysAction extends WeaverBaseEntity<SysAction> {

    @TableField("`action`")
    private String action;

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
