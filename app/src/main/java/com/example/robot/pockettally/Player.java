package com.example.robot.pockettally;

public class Player {

    private int playerId;
    private String name;
    private int avatar;
    private String fragmentTag;

    private Boolean[] ClosedMarks = new Boolean[7];

    private int[] ScoreValues = new int[7];

    private int totalScore;

    public Player(int PlayerID, String fragTag){
        playerId = PlayerID;
        fragmentTag = fragTag;

        for (int i = 0; i < ClosedMarks.length; i++){
            ClosedMarks[i] = false;
        }

        ScoreValues[0] = Scoreboard.TALLY_MARK_20_VALUE;
        ScoreValues[1] = Scoreboard.TALLY_MARK_19_VALUE;
        ScoreValues[2] = Scoreboard.TALLY_MARK_18_VALUE;
        ScoreValues[3] = Scoreboard.TALLY_MARK_17_VALUE;
        ScoreValues[4] = Scoreboard.TALLY_MARK_16_VALUE;
        ScoreValues[5] = Scoreboard.TALLY_MARK_15_VALUE;
        ScoreValues[6] = Scoreboard.TALLY_MARK_BULLS_VALUE;

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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
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

    public int[] getScoreValues() {
        return ScoreValues;
    }

    public void tallyMarkClosed(int scoreValue){

        for(int i = 0; i < ScoreValues.length; i++){
            int currentScoreValue = ScoreValues[i];
            if(currentScoreValue == scoreValue){
                ClosedMarks[i] = true;
            }

        }
    }

    public boolean isTallyMarkClosed(int scoreValue){
        boolean currentTallyMarkCondition = false;

        for(int i = 0; i < ScoreValues.length; i++){
            int currentScoreValue = ScoreValues[i];
            if(currentScoreValue == scoreValue){
                currentTallyMarkCondition = ClosedMarks[i];
            }
        }
        return currentTallyMarkCondition;
    }
    public void tallyMarkUnClosed(int scoreValue){

        for(int i = 0; i < ScoreValues.length; i++){
            int currentScoreValue = ScoreValues[i];
            if(currentScoreValue == scoreValue){
                ClosedMarks[i] = false;
            }
        }
    }

    public boolean getTallyMarkCondition(int scoreValue){

        boolean tallyMarkCondition = false;

        for(int i = 0; i < ScoreValues.length; i++) {
            int currentScoreValue = ScoreValues[i];
            if (currentScoreValue == scoreValue) {
                tallyMarkCondition = ClosedMarks[i];
            }
        }
        return tallyMarkCondition;
    }

    public boolean checkAllTallyMarks(){
        for(boolean b : ClosedMarks) if(!b) return false;
        return true;
    }

    public void resetPlayer(){
        for(int i = 0; i < ClosedMarks.length; i++){
            ClosedMarks[i] = false;
        }
        totalScore = 0;
    }

}
