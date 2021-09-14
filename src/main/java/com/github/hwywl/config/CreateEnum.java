package com.github.hwywl.config;

/**
 * @author: YI
 * @description: 返回信息的枚举类
 * @date: create in 2021/2/27 12:06
 */
public enum CreateEnum {
    JSON_ANALYSIS_ERROR(1001, "JSON解析错误!"),
    JOIN_CONDITION_ERROR(1002, "连接条件不能为空!"),
    UNKNOWN_ERROR(9999, "未知的错误!");

    private Integer code;
    private String message;

    CreateEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CreateEnum fromCode(Integer code) {
        for (CreateEnum telecomType : values()) {
            if (telecomType.code.equals(code)) {
                return telecomType;
            }
        }
        return null;
    }

    public static CreateEnum fromMessage(String message) {
        for (CreateEnum telecomType : values()) {
            if (telecomType.message.equals(message)) {
                return telecomType;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
