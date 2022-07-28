package com.lshy.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态（默认0 - 正常）
     */
    private Integer userStatus;

    /**
     * 数据是否删除（默认0 - 未删除）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间（数据插入时间）
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 用户角色
        0 - 普通用户
        1 - 管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}