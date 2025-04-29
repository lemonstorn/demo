package com.weaver.uims.user.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weaver.db.model.WeaverBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
@TableName("sys_user")
@Schema(name = "SysUser", description = "")
public class SysUser extends WeaverBaseEntity<SysUser> {

    @TableField("user_name")
    private String userName;

    @TableField("`password`")
    private String password;

    @TableField("nick_name")
    private String nickName;

    @TableField("grade_level")
    private String gradeLevel;

    @TableField("phone_num")
    private String phoneNum;

    @TableField("bank_card_num")
    private String bankCardNum;

    @TableField("domicile_place_province")
    private String domicilePlaceProvince;

    @TableField("domicile_place_city")
    private String domicilePlaceCity;

    @TableField("domicile_place_address")
    private String domicilePlaceAddress;

    @TableField("id_card_num")
    private String idCardNum;

    @TableField("gender")
    private String gender;

    @TableField("birth")
    private LocalDateTime birth;

    @TableField("avatar")
    private String avatar;

    @TableField("`source`")
    private String source;

    @TableField("`status`")
    private Integer status;

    @TableField("account_expire_time")
    private LocalDateTime accountExpireTime;

    @TableField("credentials_expire_time")
    private LocalDateTime credentialsExpireTime;

    @TableField("real_name")
    private String realName;

    @TableField("email")
    private String email;

    @TableField("tenant_id")
    private Long tenantId;

    public SysUser findLoginUser(String userName, String password){
        return selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName,userName)
                .eq(SysUser::getPassword,password)
        );
    }
}
