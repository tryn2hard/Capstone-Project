package com.example.robot.pockettally.database;


import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;

public class MarkCountsConverter {

    @TypeConverter
    public static int[] toCountArray(String counts){
        String [] parts = counts.split(" ");

        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            array[i] = Integer.parseInt(parts[i]);

        return array;
    }

    @TypeConverter
    public static String toCountString(int[] counts){
        return Arrays.toString(counts);
    }
}
