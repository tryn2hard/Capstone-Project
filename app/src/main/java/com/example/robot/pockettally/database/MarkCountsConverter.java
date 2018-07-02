package com.example.robot.pockettally.database;


import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;

public class MarkCountsConverter {

    @TypeConverter
    public static int[] toCountArray(String counts){

        String [] parts = counts.substring(1, counts.length() - 1).split(" ");
        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String smallerParts = parts[i].substring(0,1);
            array[i] = Integer.parseInt(smallerParts);
        }
        return array;
    }

    @TypeConverter
    public static String toCountString(int[] counts){
        return Arrays.toString(counts);
    }
}
