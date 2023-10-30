package com.qianye.usercenter.model;

import lombok.Data;

/**
 * @Author 浅夜
 * @Description TODO
 * @DateTime 2023/10/26 22:13
 **/
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}