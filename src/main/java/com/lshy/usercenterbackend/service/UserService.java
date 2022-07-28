package com.lshy.usercenterbackend.service;

import com.lshy.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author liushiyuan
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-06-09 15:20:20
*/
public interface UserService extends IService<User> {

    /**
     *  用户注册接口
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录接口
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request 用户端发送的请求
     * @return 脱敏后的用户对象
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏接口
     * @param user 传入的原用户对象
     * @return 脱敏后的用户对象
     */
    User getSafetyUser(User user);
}
