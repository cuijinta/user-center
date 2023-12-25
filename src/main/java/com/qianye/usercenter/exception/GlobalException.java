package com.qianye.usercenter.exception;

import com.qianye.usercenter.constant.ErrorCode;

/**
 * @Author 浅夜
 * @Description 自定义异常类
 * @DateTime 2023/12/22 23:29
 **/
public class GlobalException extends RuntimeException {

    private final int code;

    private final String description;

    public GlobalException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public GlobalException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }



}
