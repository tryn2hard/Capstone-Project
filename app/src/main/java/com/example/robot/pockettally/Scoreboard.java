package com.example.robot.pockettally;

import android.widget.ImageView;

public class Scoreboard {

    // Constants
    public final static int TALLY_MARK_20_VALUE = 20;
    public final static int TALLY_MARK_19_VALUE = 19;
    public final static int TALLY_MARK_18_VALUE = 18;
    public final static int TALLY_MARK_17_VALUE = 17;
    public final static int TALLY_MARK_16_VALUE = 16;
    public final static int TALLY_MARK_15_VALUE = 15;
    public final static int TALLY_MARK_BULLS_VALUE = 25;

    public final static int MAX_NUM_OF_TALLY_MARKS = 3;
    public final static int SINGLE_TALLY_MARK = 1;
    public final static int DOUBLE_TALLY_MARK = 2;
    public final static int TRIPLE_TALLY_MARK = 3;

    private int mValue;
    private int mCount = 0;
    private ImageView mImageView;
    private boolean mIsClosedOut = false;

    public boolean ismClosedOutByAll() {
        return mClosedOutByAll;
    }

    public void setmClosedOutByAll(boolean mClosedOutByAll) {
        this.mClosedOutByAll = mClosedOutByAll;
    }

    private boolean mClosedOutByAll = false;
    
    public Scoreboard(int value, ImageView imageView) {
        mValue = value;
        mImageView = imageView;
    }

    public int getValue() {
        return mValue;
    }

    public int getCount() {
        return mCount;
    }

    public void incrementCount(int increment) {
        mCount += increment;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public boolean isClosedOut() {
        return mIsClosedOut;
    }

    public void setClosedOut(boolean closedOut) {
        mIsClosedOut = closedOut;
    }

    public void setCount (int count){mCount = count;}
}