package org.example;

import org.example.strategy.HashBiMapStorageStrategy;
import org.example.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {
    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());

        Set<String> origStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }

        Set<Long> ids1 = new HashSet<>();
        Set<String> strings1 = new HashSet<>();
        long stringToIdTime1 = getTimeToGetIds(shortener1, origStrings, ids1);
        long idToStringTime1 = getTimeToGetStrings(shortener1, ids1,strings1);

        Set<Long> ids2 = new HashSet<>();
        Set<String> strings2 = new HashSet<>();
        long stringToIdTime2 = getTimeToGetIds(shortener2,origStrings,ids2);
        long idToStringTime2 = getTimeToGetStrings(shortener2,ids2,strings2);

        Assert.assertTrue(stringToIdTime1>stringToIdTime2);
        Assert.assertEquals(idToStringTime1,idToStringTime2,30);

    }


    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date startTimesramp = new Date();
        for(String s :strings){
            ids.add(shortener.getId(s));
        }
        Date endTimestamps = new Date();

        return endTimestamps.getTime()-startTimesramp.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener,  Set<Long> ids, Set<String> strings) {
        Date startTimesramp = new Date();
        for(Long s :ids){
            strings.add(shortener.getString(s));
        }
        Date endTimestamps = new Date();

        return endTimestamps.getTime()-startTimesramp.getTime();
    }
}
