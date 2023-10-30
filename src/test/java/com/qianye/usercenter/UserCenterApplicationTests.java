package com.qianye.usercenter;

import com.qianye.usercenter.mapper.UserMapper;
import com.qianye.usercenter.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @org.junit.Test
    public void testSelect() {
        System.out.println("select Test");
        List<User> users = userMapper.selectList(null);
        Assert.assertEquals(5, users.size());
        users.forEach(System.out::println);
    }

}
