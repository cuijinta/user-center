package com.qianye.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianye.usercenter.model.User;
import com.qianye.usercenter.service.UserService;
import com.qianye.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 浅夜光芒万丈
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-10-30 22:41:32
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    UserMapper userMapper;

    /**
     * 盐值 ，用于混淆密码
     */
    private static final String SALT = "line";

    /**
     * 用户登录状态键
     */
    private static final String USER_LOGIN_STATUS = "userLoginStatus";


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验(采用apache commons lang依赖中的方法来一次判断多个变量是否为空)
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }
        if (userAccount.length() < 4) {
            return -1L;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1L;
        }

        //账户不能包含特殊字符
        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return -1L;
        }

        //保持两次密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1L;
        }

        //账户不能重复（写在校验特殊字符逻辑之后，减小性能开销）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1L;
        }

        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean isSave = this.save(user);

        if (!isSave) {
            return -1L;
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  账户名
     * @param userPassword 账户密码
     * @return 返回脱敏后的用户信息
     */
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验(采用apache commons lang依赖中的方法来一次判断多个变量是否为空)
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        //账户不能包含特殊字符
        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        //1. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //2.查询是否存在响应的用户
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("userAccount", userAccount);
        queryWrapper1.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper1);
        if (user == null) { //用户不存在
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        //3.用户信息脱敏
        User safetyUser = getSafetyUser(user);

        //4.记录用户的用户态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        if(user == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setEmail(user.getEmail());

        return safetyUser;
    }
}




