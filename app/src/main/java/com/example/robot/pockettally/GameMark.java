package com.example.robot.pockettally;

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
