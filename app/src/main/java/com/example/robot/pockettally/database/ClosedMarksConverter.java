package com.example.robot.pockettally.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;

public class ClosedMarksConverter {
    @TypeConverter
    public static Boolean[] toArray(String tallyConditions){
        String[] parts = tallyConditions.split(" ");

        Boolean [] array = new Boolean[parts.length];
        for (int i = 0; i < parts.length; i++)
            array[i] = Boolean.parseBoolean(parts[i]);

        return array;
    }

    @TypeConverter
    public static String toString(Boolean[] closedTallyMarks){
        return Arrays.toString(closedTallyMarks);
    }
}
