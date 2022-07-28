package com.lshy.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求类
 *
 * @author liushiyuan
 */

@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    @Serial
    private static final long serialVersionUID = 1511427408934636735L;
}
