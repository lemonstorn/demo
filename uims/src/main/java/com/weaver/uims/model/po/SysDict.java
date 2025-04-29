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
@TableName("sys_dict")
@Schema(name = "SysDict", description = "")
public class SysDict extends WeaverBaseEntity<SysDict> {

    @TableField("`source`")
    private String source;

    @TableField("dict_type")
    private String dictType;

    @TableField("dict_sub_type")
    private String dictSubType;

    @TableField("dict_key")
    private String dictKey;

    @TableField("dict_value")
    private String dictValue;

    @TableField("dict_key_order")
    private Integer dictKeyOrder;

    @TableField("`status`")
    private String status;

    @TableField("dict_lang")
    private String dictLang;

    @TableField("dict_desc")
    private String dictDesc;

    @TableField("dict_extend1")
    private String dictExtend1;

    @TableField("dict_extend2")
    private String dictExtend2;

    @TableField("`order`")
    private Integer order;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
