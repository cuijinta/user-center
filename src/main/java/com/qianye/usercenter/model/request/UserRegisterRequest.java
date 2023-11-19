package com.qianye.usercenter.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 浅夜
 * @Description 用户注册请求体
 * @DateTime 2023/11/19 22:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
