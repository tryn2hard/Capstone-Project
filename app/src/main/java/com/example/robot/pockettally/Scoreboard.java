package com.example.robot.pockettally;

import android.widget.ImageView;

public class Scoreboard {

    private int mValue;
    private int mCount = 0;
    private ImageView mImageView;
    private boolean mIsClosedOut = false;
    
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
}