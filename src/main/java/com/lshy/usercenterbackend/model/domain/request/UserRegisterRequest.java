package com.lshy.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求类
 *
 * @author liushiyuan
 */

@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

    @Serial
    private static final long serialVersionUID = -7808824721752787166L;
}
