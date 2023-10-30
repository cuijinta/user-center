package com.qianye.usercenter;

import com.qianye.usercenter.mapper.UserMapper;
import com.qianye.usercenter.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 浅夜
 * @Description 测试
 * @DateTime 2023/10/26 22:16
 **/
@SpringBootTest
@RunWith(SpringRunner.class) //如果非主测试类中不加这个注解，那么单元测试不会调用SpringBoot，只是单纯的测试
public class SimpleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println("select Test");
        List<User> users = userMapper.selectList(null);
        Assert.assertEquals(5, users.size()); //单元测试断言
        users.forEach(System.out::println);
    }

}
