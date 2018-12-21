package com.wuwei.redis;

public enum RedisKeyEnum {

    PICCPERSON("PICCPERSONDATA", "person"), PICCBAODAN("PICCBAODANDATA", "baodan"), PICCCAR("PICCCARDATA", "car");
    // 成员变量  
    private String name;
    private String key;

    // 构造方法  
    private RedisKeyEnum(String name, String key) {
        this.name = name;
        this.key = key;
    }

    // 普通方法  
    public static String getName(String key) {
        for (RedisKeyEnum k : RedisKeyEnum.values()) {
            if (k.getKey() == key) {
                return k.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
