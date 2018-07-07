package com.example.robot.pockettally.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;

public class ClosedMarksConverter {

    private final static int LAST_INDEX = 1;

    @TypeConverter
    public static boolean[] toArray(String tallyConditions){
        String[] parts = tallyConditions.substring(1, tallyConditions.length() - 1).split(" ");
        boolean [] array = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String smallerParts = parts[i].substring(0, parts[i].length()- LAST_INDEX);
            array[i] = Boolean.parseBoolean(smallerParts);
        }
        return array;
    }

    @TypeConverter
    public static String toString(boolean[] closedTallyMarks){
        return Arrays.toString(closedTallyMarks);
    }
}
