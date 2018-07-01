package com.example.robot.pockettally;

/**
 * A simple POJO used to the store the instance of a single tally being marked. This class is mainly
 * stored into an array called GameHistory and call used during the un-marking of an erroneous mark.
 */

public class GameMark {

    private String mTag;
    private int mValue;
    private int mMarkMultiple;

    public GameMark(String tag, int value, int multiple){
        this.mTag = tag;
        this.mValue = value;
        this.mMarkMultiple = multiple;
    }

    public String getmTag() {
        return mTag;
    }

    public int getmValue() {
        return mValue;
    }

    public int getmMarkMultiple() {
        return mMarkMultiple;
    }
}
