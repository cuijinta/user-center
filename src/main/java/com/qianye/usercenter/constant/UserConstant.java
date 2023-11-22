package com.qianye.usercenter.constant;

/**
 * @Author 浅夜
 * @Description TODO
 * @DateTime 2023/11/22 22:59
 **/

/**
 * 用户常量
 */
public interface UserConstant {
    /**
     * 用户登录态 键
     */
    String USER_LOGIN_STATUS = "userLoginStatus";

    // ------------权限-------------
    /**
     * 普通用户
     */
    int DEFAULT__ROLE = 0;
    /**
     * 管理员
     */
    int ADMIN_ROLE = 1;
}
