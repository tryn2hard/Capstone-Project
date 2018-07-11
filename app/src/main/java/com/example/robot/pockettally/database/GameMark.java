package com.example.robot.pockettally.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * A simple POJO used to the store the instance of a single tally being marked. This class is mainly
 * stored into an array called GameHistory and call used during the un-marking of an erroneous mark.
 */

@Entity(tableName = "gameMarks")
public class GameMark {


    @PrimaryKey(autoGenerate = true)
    private int markID;

    private String tag;
    private int value;
    private int increment;



    public GameMark(String tag, int value, int increment){
        this.tag = tag;
        this.value = value;
        this.increment = increment;
    }

    public int getMarkID() {
        return markID;
    }

    public void setMarkID(int markID) {
        this.markID = markID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

}
