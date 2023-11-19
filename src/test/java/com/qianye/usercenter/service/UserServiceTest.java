package com.qianye.usercenter.service;
import java.util.Date;

import com.qianye.usercenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @Author 浅夜
 * @Description 用户服务测试
 * @DateTime 2023/10/30 22:54
 **/
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() {

        User user = new User();
        user.setUsername("test1");
        user.setUserAccount("1232");
        user.setAvatarUrl("https://web-tilas-qianye.oss-cn-hangzhou.aliyuncs.com/cb2dd958-7a54-45fa-90de-7644ad6cd426.jpg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("19119540983");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(result);
        Assertions.assertTrue(result);
    }

    /**
     * 加密测试（用于密码）
     */
    @Test
    public void testEncrypt() {
        byte[] bytes = "line12345678".getBytes();
        String newPassword = DigestUtils.md5DigestAsHex(bytes);
        System.out.println(newPassword);
    }

    @Test
    void userRegister() {
        String userAccount = "qinaye";
        String userPassword = "12345678";
        String checkedPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试非空
        userAccount = "";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试账户名不小于6位
        userAccount = "qi";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试密码不足8位
        userAccount = "qianye";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试密码两次不一致
        userPassword = "123456789";
        checkedPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试账号不能重复
        userAccount = "123";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertEquals(-1L, result);

        //测试注册功能是否正常
        userAccount = "qianye";
        result = userService.userRegister(userAccount, userPassword, checkedPassword);
        Assertions.assertTrue(result > 0);


    }
}
