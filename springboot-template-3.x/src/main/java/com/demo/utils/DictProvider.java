package com.demo.utils;

public interface DictProvider {
    /**
     * @param dictType 如 "sys_user_sex"
     * @param value    原始值，如 "0" / "1"
     * @return 展示标签，如 "男" / "女"；若找不到返回 null
     */
    String getLabel(String dictType, String value);
}
