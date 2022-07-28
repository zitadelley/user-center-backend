package com.lshy.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lshy.usercenterbackend.commons.BaseResponse;
import com.lshy.usercenterbackend.commons.ErrorCode;
import com.lshy.usercenterbackend.commons.ResultUtils;
import com.lshy.usercenterbackend.exception.BusinessException;
import com.lshy.usercenterbackend.model.domain.User;
import com.lshy.usercenterbackend.model.domain.request.UserLoginRequest;
import com.lshy.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.lshy.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lshy.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.lshy.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author liushiyuan
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://43.138.151.92" }, allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);

    }

    @GetMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){

        if (request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        int result = userService.userLogout(request);
        return ResultUtils.success(result);

    }

    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String keywords, HttpServletRequest request){

        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keywords))
            queryWrapper.like("username", keywords);

        List<User> userList = userService.list(queryWrapper);

        List<User> searchResult = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(searchResult);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long id, HttpServletRequest request){

        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);

        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        boolean deleteResult = userService.removeById(id);
        return ResultUtils.success(deleteResult);

    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){

        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        Long userId = currentUser.getId();
        //todo 判断用户是否被封号

        User user = userService.getById(userId);
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);

    }

    private boolean isAdmin(HttpServletRequest request){

        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null || user.getUserRole() != ADMIN_ROLE)
            throw new BusinessException(ErrorCode.NO_AUTH);

        return true;

    }

}
