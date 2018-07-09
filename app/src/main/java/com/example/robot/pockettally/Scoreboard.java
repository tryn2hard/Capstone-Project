package com.example.robot.pockettally;

import android.widget.ImageView;

public class Scoreboard {

    public final static int MAX_NUM_OF_TALLY_MARKS = 3;
    public final static int SINGLE_TALLY_MARK = 1;
    public final static int DOUBLE_TALLY_MARK = 2;
    public final static int TRIPLE_TALLY_MARK = 3;
    public final static boolean SCOREBOARD_OPEN = false;
    public final static boolean SCOREBOARD_CLOSED = true;

    private int mValue;
    private int mCount = 0;
    private ImageView mImageView;
    private boolean mIsClosedOut = SCOREBOARD_OPEN;
    private boolean mClosedOutByAll = SCOREBOARD_OPEN;

    public Scoreboard(int value, ImageView imageView) {
        mValue = value;
        mImageView = imageView;
    }

    public int getValue() {
        return mValue;
    }
    public ImageView getImageView() {
        return mImageView;
    }

    public void setCount (int count){mCount = count;}
    public int getCount() {
        return mCount;
    }
    public void incrementCount(int increment) {
        mCount += increment;
    }

    public boolean isClosedOut() {
        return mIsClosedOut;
    }
    public void setClosedOut(Boolean closedOut) {
        mIsClosedOut = closedOut;
    }

    public void setClosedOutByAll(boolean mClosedOutByAll) {
        this.mClosedOutByAll = mClosedOutByAll;
    }
    public boolean isClosedOutByAll() {
        return mClosedOutByAll;
    }
}