package com.wuwei.util;

/**
 * 结果码枚举
 */
public enum ResultCode {

    /**
     * 通用的返回码
     */
    COMMON_SUCCESS("1","成功"),
    COMMON_EXCEPTION("0","失败");

    /**
     * 结果码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    private ResultCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
