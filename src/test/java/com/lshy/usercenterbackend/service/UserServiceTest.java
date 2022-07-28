package com.lshy.usercenterbackend.service;

import com.lshy.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService测试
 *
 * @author liushiyuan
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void addUserTest(){
        User user = new User();

        user.setUserAccount("araragi");
        user.setUserPassword("123456");
        user.setAvatarUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F17%2F20210717232533_2edcf.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1657375457&t=fc1291a82503a00a506558501cb770e4");
        user.setGender(0);
        user.setPhone("15255465801");
        user.setEmail("araragi@163.com");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        System.out.println(user.getUserAccount());

        boolean saveResult = userService.save(user);

        Assertions.assertTrue(saveResult);

//        return saveResult;
    }

    @Test
    void userRegister() {
        
        //重复注册一个已有用户
        long duplicateRegister = userService.userRegister("araragi", "123456", "123456", "1234567");
        Assertions.assertEquals(-1, duplicateRegister);

        //新注册一个用户
        String userAccount = "hanekawa";
        String userPassword = "123456";
        String checkPassword = "123456";
        String planetCode = "1234567";

        long registerResult = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, registerResult);

        //都为空
        long res1 = userService.userRegister("", "", "", "2");
        Assertions.assertEquals(-1, res1);

        //用户名小于4个字符
        long res2 = userService.userRegister("ls", "123456", "123456", "2");
        Assertions.assertEquals(-1, res2);

        //密码小于6位
        long res3 = userService.userRegister("chenyx", "12345", "12345", "2");
        Assertions.assertEquals(-1, res3);

        //账号包含特殊字符
        long res4 = userService.userRegister("lsy 1234", "123456", "123456", "2");
        Assertions.assertEquals(-1, res4);

        //密码和确认密码不一致
        long res5 = userService.userRegister("weizhentian", "123456", "123457", "2");
        Assertions.assertEquals(-1, res5);


    }
}