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
@Entity
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int playerId;
    private String name;
    private int avatar;
    private String fragmentTag;
    private Boolean[] ClosedMarks = new Boolean[7];
    private Boolean[] AllClosedOut = new Boolean[7];
    private int[] TallyCounts = new int[7];
    private int totalScore;

    public Player(String fragmentTag){
        this.fragmentTag = fragmentTag;
        for(int i = 0; i < ClosedMarks.length; i++){
            ClosedMarks[i] = false;
        }
    }

    @Ignore
    public Player(int PlayerID, String fragTag){
        this.playerId = PlayerID;
        fragmentTag = fragTag;

        for (int i = 0; i < ClosedMarks.length; i++){
            ClosedMarks[i] = false;
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

    public Boolean[] getClosedMarks() {
        return ClosedMarks;
    }

    public void setClosedMarks(Boolean[] closedMarks) {
        ClosedMarks = closedMarks;
    }

    public Boolean[] getAllClosedOut() {
        return AllClosedOut;
    }

    public void setAllClosedOut(Boolean[] allClosedOut) {
        AllClosedOut = allClosedOut;
    }

    public int[] getTallyCounts() {
        return TallyCounts;
    }

    public void setTallyCounts(int[] tallyCounts) {
        TallyCounts = tallyCounts;
    }

}
