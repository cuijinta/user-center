package com.qianye.usercenter.service;

import com.qianye.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 浅夜光芒万丈
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-30 22:41:32
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 账户名
     * @param userPassword 账户密码
     * @return 返回脱敏后的用户信息
     */
    User  doLogin(String userAccount, String userPassword, HttpServletRequest request);
}
