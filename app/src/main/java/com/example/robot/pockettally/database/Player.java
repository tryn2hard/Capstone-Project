package com.example.robot.pockettally.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.robot.pockettally.ScoreboardUtils;

/**
 * The Player class stores all the information for an individual player. Including name, avatar,
 * id, and fragment tag. The player class has additional methods to help check if a player has
 * closed out a mark or if the player has finished.
 */
@Entity(tableName = "players")
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int playerId;

    private String name;
    private int avatar;
    private String fragmentTag;
    private boolean[] ClosedMarks = new boolean[7];
    private boolean[] AllClosedOut = new boolean[7];
    private int[] TallyCounts = new int[7];
    private int totalScore;

    public Player(String fragmentTag){
        this.fragmentTag = fragmentTag;
        this.name = null;
        this.avatar = 0;
        this.totalScore = 0;

        for(int i = 0; i < ClosedMarks.length; i++){
            this.ClosedMarks[i] = false;
            this.AllClosedOut[i] = false;
            this.TallyCounts[i] = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public boolean[] getClosedMarks() {
        return ClosedMarks;
    }

    public void setClosedMarks(boolean[] closedMarks) {
        ClosedMarks = closedMarks;
    }

    public boolean[] getAllClosedOut() {
        return AllClosedOut;
    }

    public void setAllClosedOut(boolean[] allClosedOut) {
        AllClosedOut = allClosedOut;
    }

    public int[] getTallyCounts() {
        return TallyCounts;
    }

    public void setTallyCounts(int[] tallyCounts) {
        TallyCounts = tallyCounts;
    }

}
