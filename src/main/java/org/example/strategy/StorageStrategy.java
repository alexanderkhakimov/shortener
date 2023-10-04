package org.example.strategy;

public interface StorageStrategy {
    boolean containsKey(Long key);
    boolean containsValue(String value);
    void put(Long key, String value);
    Long getKey(String velue);
    String getValue(Long key);
}
