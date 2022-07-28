package com.lshy.usercenterbackend.exception;

import com.lshy.usercenterbackend.commons.ErrorCode;
import lombok.Data;

import java.io.Serial;

/**
 * @author liushiyuan 自定义异常类
 */
@Data
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();

    }

    public BusinessException(ErrorCode errorCode, String description) {

        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;

    }

    @Serial
    private static final long serialVersionUID = 5786768529263360794L;
}
