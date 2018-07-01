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

    public Player(){

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

    /**
     * Method will notify this player that a tally mark has been closed out.
     * @param scoreValue the integer value of the tally mark that has been closed out
     */
    public void tallyMarkClosed(int scoreValue){
        ClosedMarks[ScoreboardUtils.matchScoreValue(scoreValue)] = true;
    }

    /**
     * Method will notify this player that the tally mark is no longer closed as a result of the
     * undo button being pressed by the user.
     * @param scoreValue the integer value of the tally mark that has been opened
     */
    public void tallyMarkUnClosed(int scoreValue){
        ClosedMarks[ScoreboardUtils.matchScoreValue(scoreValue)] = false;
    }

    /**
     * Method will return the status of a specific tally mark
     * @param scoreValue the integer value of the tally mark in question
     * @return boolean value of the current condition of the tally mark
     */
    public boolean isTallyMarkClosed(int scoreValue){
        return ClosedMarks[ScoreboardUtils.matchScoreValue(scoreValue)];
    }

    /**
     * Method will check the current condition of all the tally marks for this player.
     * @return boolean value of true will be returned if all the marks are closed.
     */
    public boolean checkAllTallyMarks(){
        for(boolean b : ClosedMarks) if(!b) return false;
        return true;
    }

    /**
     * Method will reset this player to a start of game state. Usually done when the game has ended
     * or when the user presses the reset button
     */
    public void resetPlayer(){
        for(int i = 0; i < ClosedMarks.length; i++){
            ClosedMarks[i] = false;
        }
        totalScore = 0;
    }
}
