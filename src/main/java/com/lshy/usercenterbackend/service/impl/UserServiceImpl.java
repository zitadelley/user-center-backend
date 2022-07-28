package com.lshy.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lshy.usercenterbackend.commons.ErrorCode;
import com.lshy.usercenterbackend.exception.BusinessException;
import com.lshy.usercenterbackend.model.domain.User;
import com.lshy.usercenterbackend.service.UserService;
import com.lshy.usercenterbackend.Mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lshy.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author liushiyuan
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-06-09 15:20:20
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 加密的盐值
     */
    private static final String SALT = "lshy";

    /**
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {

        //都不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空！");

        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4！");

        if (userPassword.length() < 6 || checkPassword.length() < 6)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于6！");

        if (planetCode.length() > 6)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号长度大于6！");

        //账号不能包含特殊字符
        String invalidChar = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(invalidChar).matcher(userAccount);
        if (matcher.find())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符！");

        //密码和确认密码相同
        if ( !userPassword.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和确认密码不相同！");

        //账号不能和已有账号一样
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        long count = this.count(userQueryWrapper);
        if (count > 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能和已有账号一样！");

        //星球编号不能和已有的编号一样
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("planetCode", planetCode);
        long planetCount = this.count(userQueryWrapper);
        if (planetCount > 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不能和已有的编号一样！");

        //信息校验完成，接下来加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);

        //将user各个属性对应到字段，存入数据库中
        boolean result = this.save(user);

        if ( !result )
            throw new BusinessException(ErrorCode.DB_OPERATION_ERROR, "用户信息未能保存到数据库中！");

        //如果上述操作都成功
        return user.getId();
    }

    /**
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request 用户端发送的请求
     * @return 脱敏后的用户对象
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //都不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空！");

        //账号的长度不能小于4
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4！");

        //密码的长度不能小于6
        if (userPassword.length() < 6)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于6！");

        //账号不能包含特殊字符
        String invalidChar = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(invalidChar).matcher(userAccount);
        if (matcher.find())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符！");

        //账号必须在数据库内
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        long count = this.count(userQueryWrapper);
        if (count == 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不在数据库内！");

        //密码必须能和对应的账号相匹配
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(userQueryWrapper);
        if (user == null)
        {
//            log.info("userpassword does not match useraccount");
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "账号和密码不匹配！");
        }


        //如果账号和密码都能匹配成功，进行用户信息脱敏，并返回脱敏后的用户信息
        User safetyUser = getSafetyUser(user);


        //记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        //如果上述操作都成功
        return safetyUser;


    }

    @Override
    public int userLogout(HttpServletRequest request) {

        request.getSession().removeAttribute(USER_LOGIN_STATE);

        return 1;
    }

    /**
     * 用户脱敏
     * @param user 传入的原用户对象
     * @return 脱敏后的用户对象
     */
    @Override
    public User getSafetyUser(User user){

        if (user == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户对象为空！");

        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setPlanetCode(user.getPlanetCode());

        return safetyUser;

    }

}




