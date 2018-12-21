package com.wuwei.entity;

import java.io.Serializable;

/**
 * 用于封装响应结果
 *
 * @author 吴维
 * @date 2017-8-6 10:49:48
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private int status;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
