package com.wuwei.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**<p>com.mdc.mansion.common.response.Result</p>
 * <p>Title: Result</p>
 * <p>Copyright: Copyright © 2010-2014</p>
 * <p>Company: jumei</p>
 * @author xiang.liu
 * @version 1.0
 */
public class Result implements Serializable{


    private static final long serialVersionUID = 1L;

    /**
     * 结果码
     */
    private String code;

    private String reqId;

    /**
     * 自定义属性
     */
    private Map<String, Object> resultMap = new HashMap<String, Object>();
    /**
     * 描述
     */
    private Object description;

    public Result() {

    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.description = resultCode.getDescription();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
    /**
     * 获取结果中某个属性的值
     *
     * @param propertyName 属性的名称
     * @return 属性的值
     */
    public Object getProperty(String propertyName) {
        return this.resultMap.get(propertyName);
    }

    /**
     * 设置某个属性
     *
     * @param propertyName 属性的名称
     * @param propertyValue 属性的值
     * @return 如果已经存在属性，则返回的是现有的属性值；否则返回null
     */
    public Object setProperty(String propertyName, Object propertyValue) {
        return this.resultMap.put(propertyName, propertyValue);
    }

    public void removeProperty(String propertyName){
        this.resultMap.remove(propertyName);
    }

    public void removeAllProperty(){
        this.resultMap.clear();
    }

    public void setCode(ResultCode resultCode){
        setCode(resultCode.getCode());
        setDescription(resultCode.getDescription());
    }



}

