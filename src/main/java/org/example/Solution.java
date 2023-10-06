package org.example;

import org.example.strategy.FileStorageStrategy;
import org.example.strategy.HashMapStorageStrategy;
import org.example.strategy.OurHashMapStorageStrategy;
import org.example.strategy.StorageStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> keys = new HashSet<Long>();

        for (String s : strings) {
            keys.add(shortener.getId(s));
        }
        return keys;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> strings = new HashSet<String>();

        for (Long key : keys) {
            strings.add(shortener.getString(key));
        }
        return strings;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());

        Set<String> strings = new HashSet<String>();

        for (int i = 0; i < elementsNumber; i++) {
            strings.add(Helper.generateRandomString());
        }

        Shortener shortener = new Shortener(strategy);

        Date startTime = new Date();
        Set<Long> keys = getIds(shortener, strings);
        Date endTime = new Date();
        long time = endTime.getTime() - startTime.getTime();
        Helper.printMessage("Время получения идентификаторов для " + elementsNumber + " строк: " + time);

        startTime = new Date();
        Set<String> stringSet = getStrings(shortener, keys);
        endTime = new Date();
        time = endTime.getTime() - startTime.getTime();
        Helper.printMessage("Время получения строк для " + elementsNumber + " идентификаторов: " + time);

        if (strings.equals(stringSet))
            Helper.printMessage("Тест пройден.");
        else
            Helper.printMessage("Тест не пройден.");

    }

    public static void main(String[] args) {
        long elements = 100;
        testStrategy(new HashMapStorageStrategy(),elements);
        testStrategy(new OurHashMapStorageStrategy(),elements);
        testStrategy(new FileStorageStrategy(),elements);
    }

}
