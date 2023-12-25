package com.qianye.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qianye.usercenter.common.Result;
import com.qianye.usercenter.constant.ErrorCode;
import com.qianye.usercenter.exception.GlobalException;
import com.qianye.usercenter.model.User;
import com.qianye.usercenter.model.request.UserLoginRequest;
import com.qianye.usercenter.model.request.UserRegisterRequest;
import com.qianye.usercenter.service.UserService;
import com.qianye.usercenter.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.qianye.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.qianye.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @Author 浅夜
 * @Description 控制层
 * @DateTime 2023/11/15 23:02
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new GlobalException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String code = userRegisterRequest.getCode();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, code)) {
            throw new GlobalException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, code);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new GlobalException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new GlobalException(ErrorCode.PARAMS_ERROR, "用户用或密码错误");
        }
        User user = userService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<Integer> userLogout(HttpServletRequest request) {
        if(request == null) {
            throw new GlobalException(ErrorCode.NOT_LOGIN);
        }
        Integer userLogout = userService.userLogout(request);
        return ResultUtils.success(userLogout);
    }

    /**
     * 获取当前用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public Result<User> currentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new GlobalException(ErrorCode.NOT_LOGIN, "请先登录");
        }
        long userId = currentUser.getId();
        //todo: 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 管理员根据用户名搜索用户
     *
     * @param username 用户名
     * @param request  请求体
     * @return
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new GlobalException(ErrorCode.NO_AUTH);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> safetyUserList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(safetyUserList);
    }

    /**
     * 根据id删除用户（逻辑删除）
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //鉴权，不是管理员不能删除
        if (!isAdmin(request)) {
            throw new GlobalException(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new GlobalException(ErrorCode.PARAMS_ERROR);
        }

        boolean isRemove = userService.removeById(id);
        return ResultUtils.success(isRemove);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权，仅管理员可查
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObject;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
