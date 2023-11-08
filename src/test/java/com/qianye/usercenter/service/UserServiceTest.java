package com.qianye.usercenter.service;
import java.util.Date;

import com.qianye.usercenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author 浅夜
 * @Description 用户服务测试
 * @DateTime 2023/10/30 22:54
 **/
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {

        User user = new User();
        user.setUsername("test");
        user.setUserAccount("123");
        user.setAvatarUrl("https://web-tilas-qianye.oss-cn-hangzhou.aliyuncs.com/cb2dd958-7a54-45fa-90de-7644ad6cd426.jpg");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("19119540983");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(result);
        Assertions.assertTrue(result);
    }
}
