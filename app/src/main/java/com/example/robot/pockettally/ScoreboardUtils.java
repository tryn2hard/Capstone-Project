package com.example.robot.pockettally;

/**
 * Class is a utility class to hold the values of the scoreboard.
 */

public final class ScoreboardUtils {

    // Constants
    public final static int TALLY_MARK_20_VALUE = 20;
    public final static int TALLY_MARK_19_VALUE = 19;
    public final static int TALLY_MARK_18_VALUE = 18;
    public final static int TALLY_MARK_17_VALUE = 17;
    public final static int TALLY_MARK_16_VALUE = 16;
    public final static int TALLY_MARK_15_VALUE = 15;
    public final static int TALLY_MARK_BULLS_VALUE = 25;


    // An array of the scoreValues
    public final static int[]ScoreValues = new int[]{
    TALLY_MARK_20_VALUE, TALLY_MARK_19_VALUE, TALLY_MARK_18_VALUE, TALLY_MARK_17_VALUE,
    TALLY_MARK_16_VALUE, TALLY_MARK_15_VALUE, TALLY_MARK_BULLS_VALUE
    };


    /**
     * A helper method to match the score value to the position in the array for closed marks.
     * @param scoreValue integer value that will be checked to match
     * @return the integer position of the score value.
     */
    public static int matchScoreValue(int scoreValue){
        int scoreValuePosition = 0;
        for (int i = 0; i < ScoreValues.length; i++){
            int currentScoreValue;
            currentScoreValue = ScoreValues[i];
            if(currentScoreValue == scoreValue){
                scoreValuePosition = i;
            }
        }
        return scoreValuePosition;
    }
}
