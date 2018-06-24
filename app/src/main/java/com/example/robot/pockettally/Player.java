package com.example.robot.pockettally;

public class Player {

    private int playerId;
    private String name;
    private int avatar;
    private String fragmentTag;

    private Boolean tallyMark20Closed = false;
    private int tallyMark20Count;

    private Boolean tallyMark19Closed = false;
    private int tallyMark19Count;

    private Boolean tallyMark18Closed = false;
    private int tallyMark18Count;

    private Boolean tallyMark17Closed = false;
    private int tallyMark17Count;

    private Boolean tallyMark16Closed = false;
    private int tallyMark16Count;

    private Boolean tallyMark15Closed = false;
    private int tallyMark15Count;

    private Boolean tallyMarkBullsClosed = false;
    private int tallyMarkBullsCount;

    private int totalScore;

    public Player(int PlayerID, String fragTag){
        playerId = PlayerID;
        fragmentTag = fragTag;
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

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public Boolean getTallyMark20Closed() {
        return tallyMark20Closed;
    }

    public void setTallyMark20Closed(Boolean tallyMark20Closed) {
        this.tallyMark20Closed = tallyMark20Closed;
    }

    public int getTallyMark20Count() {
        return tallyMark20Count;
    }

    public void setTallyMark20Count(int tallyMark20Count) {
        this.tallyMark20Count = tallyMark20Count;
    }

    public Boolean getTallyMark19Closed() {
        return tallyMark19Closed;
    }

    public void setTallyMark19Closed(Boolean tallyMark19Closed) {
        this.tallyMark19Closed = tallyMark19Closed;
    }

    public int getTallyMark19Count() {
        return tallyMark19Count;
    }

    public void setTallyMark19Count(int tallyMark19Count) {
        this.tallyMark19Count = tallyMark19Count;
    }

    public Boolean getTallyMark18Closed() {
        return tallyMark18Closed;
    }

    public void setTallyMark18Closed(Boolean tallyMark18Closed) {
        this.tallyMark18Closed = tallyMark18Closed;
    }

    public int getTallyMark18Count() {
        return tallyMark18Count;
    }

    public void setTallyMark18Count(int tallyMark18Count) {
        this.tallyMark18Count = tallyMark18Count;
    }

    public Boolean getTallyMark17Closed() {
        return tallyMark17Closed;
    }

    public void setTallyMark17Closed(Boolean tallyMark17Closed) {
        this.tallyMark17Closed = tallyMark17Closed;
    }

    public int getTallyMark17Count() {
        return tallyMark17Count;
    }

    public void setTallyMark17Count(int tallyMark17Count) {
        this.tallyMark17Count = tallyMark17Count;
    }

    public Boolean getTallyMark16Closed() {
        return tallyMark16Closed;
    }

    public void setTallyMark16Closed(Boolean tallyMark16Closed) {
        this.tallyMark16Closed = tallyMark16Closed;
    }

    public int getTallyMark16Count() {
        return tallyMark16Count;
    }

    public void setTallyMark16Count(int tallyMark16Count) {
        this.tallyMark16Count = tallyMark16Count;
    }

    public Boolean getTallyMark15Closed() {
        return tallyMark15Closed;
    }

    public void setTallyMark15Closed(Boolean tallyMark15Closed) {
        this.tallyMark15Closed = tallyMark15Closed;
    }

    public int getTallyMark15Count() {
        return tallyMark15Count;
    }

    public void setTallyMark15Count(int tallyMark15Count) {
        this.tallyMark15Count = tallyMark15Count;
    }

    public Boolean getTallyMarkBullsClosed() {
        return tallyMarkBullsClosed;
    }

    public void setTallyMarkBullsClosed(Boolean tallyMarkBullsClosed) {
        this.tallyMarkBullsClosed = tallyMarkBullsClosed;
    }

    public int getTallyMarkBullsCount() {
        return tallyMarkBullsCount;
    }

    public void setTallyMarkBullsCount(int tallyMarkBullsCount) {
        this.tallyMarkBullsCount = tallyMarkBullsCount;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void tallyMarkClosed(int scoreValue){
        switch(scoreValue){
            case 20:
                setTallyMark20Closed(true);
                break;
            case 19:
                setTallyMark19Closed(true);
                break;
            case 18:
                setTallyMark18Closed(true);
                break;
            case 17:
                setTallyMark17Closed(true);
                break;
            case 16:
                setTallyMark16Closed(true);
                break;
            case 15:
                setTallyMark15Closed(true);
                break;
            case 25:
                setTallyMarkBullsClosed(true);
                break;
        }
    }

    public boolean getTallyMarkCondition(int scoreValue){

        boolean tallyMarkCondition = false;

        switch(scoreValue){
            case 20:
                tallyMarkCondition = getTallyMark20Closed();
                break;
            case 19:
                tallyMarkCondition = getTallyMark19Closed();
            break;
            case 18:
                tallyMarkCondition = getTallyMark18Closed();
            break;
            case 17:
                tallyMarkCondition = getTallyMark17Closed();
            break;
            case 16:
                tallyMarkCondition = getTallyMark16Closed();
            break;
            case 15:
                tallyMarkCondition = getTallyMark15Closed();
            break;
            case 25:
                tallyMarkCondition = getTallyMarkBullsClosed();
            break;
        }

        return tallyMarkCondition;
    }



}
