package com.demo.utils;


public interface DictProvider {
    DictResult getDict(String dictType, String value);
}