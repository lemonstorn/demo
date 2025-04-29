package com.weaver.db.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.weaver.core.tree.TreeId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zh
 * @CreateTime: 2025-04-13
 * @Description: 数据库模型基本结构
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeaverBaseEntity<E extends Model<E>> extends Model<E> {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @TreeId
    private Long id;
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    private String createdBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "updated_by",fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return id;
    }

    public String getIdString() {
        return String.valueOf(id);
    }
}
