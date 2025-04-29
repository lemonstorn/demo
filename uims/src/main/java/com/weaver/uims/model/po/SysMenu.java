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
@TableName("sys_menu")
@Schema(name = "SysMenu", description = "")
public class SysMenu extends WeaverBaseEntity<SysMenu> {

    @TableField("`name`")
    private String name;

    @TableField("parent_id")
    private Long parentId;

    @TableField("icon")
    private String icon;

    @TableField("redirect")
    private String redirect;

    @TableField("tile")
    private String tile;

    @TableField("`component`")
    private String component;

    @TableField("`source`")
    private String source;

    @TableField("`status`")
    private Integer status;

    @TableField("title")
    private String title;

    @TableField("`path`")
    private String path;

    @TableField("is_keep_alive")
    private Integer isKeepAlive;

    @TableField("sort")
    private Integer sort;

    @TableField("is_link")
    private String isLink;

    @TableField("is_hide")
    private Integer isHide;

    @TableField("is_full")
    private Integer isFull;

    @TableField("is_affix")
    private Integer isAffix;

    @TableField("active_menu")
    private String activeMenu;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
