package com.qianye.usercenter.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 浅夜
 * @Description 用户登录请求体
 * @DateTime 2023/11/19 22:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    private String userAccount;

    private String userPassword;
}
