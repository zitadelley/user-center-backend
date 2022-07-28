package com.lshy.usercenterbackend.commons;

public class ResultUtils {

    public static  <T> BaseResponse<T> success(T data) {

        return new BaseResponse<>(0, data, "ok", "");

    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {

        return new BaseResponse<>(errorCode);

    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {

        return new BaseResponse<>(code, message, description);

    }
}
