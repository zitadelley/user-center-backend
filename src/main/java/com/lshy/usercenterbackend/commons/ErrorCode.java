package com.lshy.usercenterbackend.commons;

public enum ErrorCode {

    SUCCESS(0, "成功", ""),
    PARAMS_ERROR(40000, "参数错误！", ""),
    NULL_ERROR(40001, "请求数据为空！", ""),
    NOT_LOGIN(40100, "未登录！", ""),
    NO_AUTH(40101, "无权限！", ""),
    DB_OPERATION_ERROR(50001, "数据库操作错误！", ""),
    SYSTEM_ERROR(50000, "服务器内部错误！", ""),

    USER_NOT_FOUND(50002, "未查找到该用户信息！", "")
    ;

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
