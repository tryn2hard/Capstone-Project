package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A {@link Fragment} to hold the tally marking logic for an individual player. PlayerFragment will
 * be hosted in the DartsGameActivity.
 */
public class PlayerFragment extends Fragment {
    PlayerProgressListener mCallback;

    private static final String LOG_TAG = "PlayerFragment";

    /**
     * A public interface used to communicate back to the host activity about changes made on the
     * tally marks.
     */
    public interface PlayerProgressListener {
        void TallyClosed(String tag, int position);

        void PlayerNamed(String tag, String name);

        void AvatarSelected(String tag, int avatar);

        void TallyMarked(String tag, int scoreValue, int multiple, int[] tallyCount);

        void TotalScoreHasChanged(String tag, int totalScore);

        void TallyOpened(String tag, int scoreValue);
    }

    /**
     * lifecycle method called ensure that the host activity has implemented a listener for the
     * interface
     *
     * @param context the host activity context from DartsGameActivity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PlayerProgressListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PlayerProgressListener");
        }
    }

    // constant string to retrieve arguments passed in from the host activity
    public final static String FRAGMENT_ARGS_GAME_MODE_KEY = "game mode";
    public final static String FRAGMENT_ARGS_VIBE_KEY = "vibe";
    public final static String FRAGMENT_ARGS_PLAYER_NAME_KEY = "name";
    public final static String FRAGMENT_ARGS_PLAYER_AVATAR_KEY = "avatar";
    public final static String FRAGMENT_ARGS_CLOSED_OUT_KEY = "closedOut";
    public final static String FRAGMENT_ARGS_ALL_CLOSED_OUT_KEY = "allClosedOut";
    public final static String FRAGMENT_ARGS_TALLY_COUNT_KEY = "mTallyCount";
    public final static String FRAGMENT_ARGS_TOTAL_SCORE_KEY = "mTotalScore";
    public final static String FRAGMENT_ARGS_GAME_INIT_KEY = "gameInitialized";

    // an array of scoreboards to hold the data for each of the tally marks.
    public Scoreboard[] scoreboards = new Scoreboard[7];

    //Binding the image views
    @BindView(R.id.scoreboard_20)
    ImageView scoreboard_20_iv;
    @BindView(R.id.scoreboard_19)
    ImageView scoreboard_19_iv;
    @BindView(R.id.scoreboard_18)
    ImageView scoreboard_18_iv;
    @BindView(R.id.scoreboard_17)
    ImageView scoreboard_17_iv;
    @BindView(R.id.scoreboard_16)
    ImageView scoreboard_16_iv;
    @BindView(R.id.scoreboard_15)
    ImageView scoreboard_15_iv;
    @BindView(R.id.scoreboard_bulls)
    ImageView scoreboard_bulls_iv;
    @BindView(R.id.divider4)
    View divider_v;
    @BindView(R.id.game_score)
    TextView game_score_tv;

    @BindView(R.id.player_avatar_image)
    ImageView player_avatar_iv;
    @BindView(R.id.editText)
    EditText name_et;

    private List<Integer> avatars;


    private Vibrator vibe;
    private final static int VIBRATE_TIME = 100;

    private String mGameMode;
    private Boolean mVibe;

    public String mName;
    public int mAvatar;
    public String mTag;
    public int[] mTallyCount = new int[7];
    public boolean[] mClosedOut = new boolean[7];
    public boolean[] mAllClosedOut = new boolean[7];
    public boolean mGameInit;
    public int mTotalScore;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, rootView);

        mTag = getTag();

        // Retrieval of the arguments sent from the host activity
        if (getArguments() != null) {
            mVibe = getArguments().getBoolean(FRAGMENT_ARGS_VIBE_KEY);
            mGameMode = getArguments().getString(FRAGMENT_ARGS_GAME_MODE_KEY);
            mGameInit = getArguments().getBoolean(FRAGMENT_ARGS_GAME_INIT_KEY);
        }

        // Removal of the views when in no points mode
        if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {
            game_score_tv.setVisibility(View.INVISIBLE);
            divider_v.setVisibility(View.INVISIBLE);
        }

        // If mName is null set the tag as the user's name, else set mName
        if (mName == null) {
            name_et.setText(getTag());
        } else {
            name_et.setText(mName);
        }

        for(int i = 0; i < mClosedOut.length; i++){
            mClosedOut[i] = false;
            mAllClosedOut[i] = false;
            mTallyCount[i] = 0;
        }

        // OnKeyListener for the edit text
        name_et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    name_et.setFocusable(false);
//                    name_et.setFocusableInTouchMode(true);
                    mName = name_et.getText().toString();
                    mCallback.PlayerNamed(mTag, mName);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Getting an instance of the vibrator
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Create an instance of the assets
        avatars = new AvatarImageAssets().getAvatars();

        // Initialize Scoreboards
        scoreboards[0] = new Scoreboard(ScoreboardUtils.TALLY_MARK_20_VALUE, scoreboard_20_iv);
        scoreboards[1] = new Scoreboard(ScoreboardUtils.TALLY_MARK_19_VALUE, scoreboard_19_iv);
        scoreboards[2] = new Scoreboard(ScoreboardUtils.TALLY_MARK_18_VALUE, scoreboard_18_iv);
        scoreboards[3] = new Scoreboard(ScoreboardUtils.TALLY_MARK_17_VALUE, scoreboard_17_iv);
        scoreboards[4] = new Scoreboard(ScoreboardUtils.TALLY_MARK_16_VALUE, scoreboard_16_iv);
        scoreboards[5] = new Scoreboard(ScoreboardUtils.TALLY_MARK_15_VALUE, scoreboard_15_iv);
        scoreboards[6] = new Scoreboard(ScoreboardUtils.TALLY_MARK_BULLS_VALUE, scoreboard_bulls_iv);

        // Set listeners on each Scoreboard
        for (int i = 0; i < scoreboards.length; i++) {
            final Scoreboard current_Scoreboard = scoreboards[i];
            final ImageView current_Scoreboard_iv = current_Scoreboard.getImageView(); // get current scoreboard's ImageView

            if(mTallyCount != null){
                current_Scoreboard.setCount(mTallyCount[i]);
                tallyImageSelector(current_Scoreboard);
            }

            current_Scoreboard_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!current_Scoreboard.isClosedOutByAll()) {
                        if (mVibe) {
                            vibe.vibrate(VIBRATE_TIME);
                        }
                        // If the game is in no points mode and the current scoreboard is not closed out
                        if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value)) && !current_Scoreboard.isClosedOut()) {
                            // update the array tracking all the tally counts
                            mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                    = current_Scoreboard.getCount() + Scoreboard.SINGLE_TALLY_MARK;
                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);
                            // notify the host activity that a mark has been made
                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                    Scoreboard.SINGLE_TALLY_MARK, mTallyCount);
                            // select the correct image for the tally mark
                            tallyImageSelector(current_Scoreboard);
                            // If the game is in standard points mode
                        } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                            // update the array tracking all the tally counts
                            mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                    = current_Scoreboard.getCount() + Scoreboard.SINGLE_TALLY_MARK;
                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);
                            // notify the host activity that a mark has been made
                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                    Scoreboard.SINGLE_TALLY_MARK, mTallyCount);
                            // if the tally mark hasn't been closed out then select the image to display,
                            // else add the current mark to the total score and notify the host activity that
                            // the total score has changed
                            if (!current_Scoreboard.isClosedOut()) {
                                tallyImageSelector(current_Scoreboard);
                            } else {
                                mTotalScore += current_Scoreboard.getValue();
                                game_score_tv.setText(String.valueOf(mTotalScore));
                                mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                            }
                        }
                    }
                }
            });

            current_Scoreboard_iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Dialog multiMarkDialog = new Dialog(getActivity());
                    multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                    multiMarkDialog.findViewById(R.id.double_mark_button)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mVibe) {
                                        vibe.vibrate(VIBRATE_TIME);
                                    }
                                    if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {
                                        if (!current_Scoreboard.isClosedOut()) {
                                            // update the array tracking all the tally counts
                                            mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                                    = current_Scoreboard.getCount() + Scoreboard.DOUBLE_TALLY_MARK;
                                            // increment the count
                                            current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);
                                            // notify the host activity that a mark has been made
                                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                                    Scoreboard.DOUBLE_TALLY_MARK, mTallyCount);
                                            tallyImageSelector(current_Scoreboard);
                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                                        // update the array tracking all the tally counts
                                        mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                                = current_Scoreboard.getCount() + Scoreboard.DOUBLE_TALLY_MARK;
                                        // increment the count
                                        current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);
                                        // notify the host activity that a mark has been made
                                        mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                                Scoreboard.DOUBLE_TALLY_MARK, mTallyCount);
                                        if (!current_Scoreboard.isClosedOut()) {
                                            tallyImageSelector(current_Scoreboard);
                                        } else {
                                            mTotalScore += current_Scoreboard.getValue() * Scoreboard.DOUBLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(mTotalScore));
                                            mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                                        }
                                    }
                                    multiMarkDialog.dismiss();
                                }
                            });

                    multiMarkDialog.findViewById(R.id.triple_mark_button)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mVibe) {
                                        vibe.vibrate(VIBRATE_TIME);
                                    }
                                    if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {
                                        if (!current_Scoreboard.isClosedOut()) {
                                            // update the array tracking all the tally counts
                                            mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                                    = current_Scoreboard.getCount() + Scoreboard.TRIPLE_TALLY_MARK;
                                            // increment the count
                                            current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);
                                            // notify the host activity that a mark has been made
                                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                                    Scoreboard.TRIPLE_TALLY_MARK, mTallyCount);
                                            tallyImageSelector(current_Scoreboard);
                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                                        // update the array tracking all the tally counts
                                        mTallyCount[ScoreboardUtils.matchScoreValue(current_Scoreboard.getValue())]
                                                = current_Scoreboard.getCount() + Scoreboard.TRIPLE_TALLY_MARK;
                                        // increment the count
                                        current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);
                                        // notify the host activity that a mark has been made
                                        mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(),
                                                Scoreboard.TRIPLE_TALLY_MARK, mTallyCount);
                                        if (!current_Scoreboard.isClosedOut()) {
                                            tallyImageSelector(current_Scoreboard);
                                        } else {
                                            mTotalScore += current_Scoreboard.getValue() * Scoreboard.TRIPLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(mTotalScore));
                                            mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                                        }
                                    }
                                    multiMarkDialog.dismiss();
                                }
                            });
                    multiMarkDialog.show();
                    return false;
                }
            });

            if(mGameInit){
                mName = getArguments().getString(FRAGMENT_ARGS_PLAYER_NAME_KEY);
                mAvatar = getArguments().getInt(FRAGMENT_ARGS_PLAYER_AVATAR_KEY);
                mClosedOut = getArguments().getBooleanArray(FRAGMENT_ARGS_CLOSED_OUT_KEY);
                mAllClosedOut = getArguments().getBooleanArray(FRAGMENT_ARGS_ALL_CLOSED_OUT_KEY);
                mTallyCount = getArguments().getIntArray(FRAGMENT_ARGS_TALLY_COUNT_KEY);
                mTotalScore = getArguments().getInt(FRAGMENT_ARGS_TOTAL_SCORE_KEY);

                reloadGame(mName, mAvatar, mClosedOut, mAllClosedOut, mTallyCount, mTotalScore);

            }
        }

        /*
        This block of code opens a dialog when the user clicks on the default avatar image
         */
        player_avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.avatar_dialog);
                List<Integer> avatars_iv = new ArrayList<Integer>() {{
                    add(R.id.avatar_man);
                    add(R.id.avatar_man_1);
                    add(R.id.avatar_man_2);
                    add(R.id.avatar_man_3);
                    add(R.id.avatar_man_4);
                    add(R.id.avatar_man_5);
                    add(R.id.avatar_man_6);
                    add(R.id.avatar_woman);
                    add(R.id.avatar_woman_1);
                }};
                for (int i = 0; i < avatars_iv.size(); i++) {
                    final ImageView currentImageView = dialog.findViewById(avatars_iv.get(i));
                    final int currentIndex = i;
                    currentImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), avatars.get(i)));
                    currentImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            player_avatar_iv.setImageResource(avatars.get(currentIndex));
                            mAvatar = avatars.get(currentIndex);
                            mCallback.AvatarSelected(mTag, mAvatar);
                            dialog.dismiss();
                            currentImageView.setImageDrawable(null);
                        }
                    });

                }
                dialog.show();
            }
        });
        return rootView;
    }

    /**
     * Method will select the proper tally mark to be displayed to the user based on the current
     * count of the scoreboard.
     * @param current_Scoreboard a instance of the scoreboard class, will be used to retrieve the
     *                           current count
     */
    private void tallyImageSelector(Scoreboard current_Scoreboard) {
        int tallyCount = current_Scoreboard.getCount();
        switch (current_Scoreboard.getCount()) {
            case 0:
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_no_marks);
                break;
            case 1:
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_one_mark);
                break;
            case 2:
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_two_marks);
                break;
            case 3:
                current_Scoreboard.setClosedOut(true);
                mCallback.TallyClosed(getTag(), current_Scoreboard.getValue());
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_three_marks);
                break;
            default:
                current_Scoreboard.setClosedOut(true);
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_three_marks);
                mCallback.TallyClosed(getTag(), current_Scoreboard.getValue());
                // if the current game mode is standard, the default should be to add the total points
                // accumulated to the total score
                if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                        && !current_Scoreboard.isClosedOutByAll()) {
                    mTotalScore += (tallyCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS) * current_Scoreboard.getValue();
                    game_score_tv.setText(String.valueOf(mTotalScore));
                    mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                }
        }
    }

    /**
     * Method will set the mClosedOutByAll boolean to true for the scoreboard associated with the scorevalue
     * @param scoreValue integer value to find the correct scoreboard
     */

    public void tallyMarkClosedOutByAll(int scoreValue) {
        for (Scoreboard scoreboard : scoreboards) {
            if (scoreValue == scoreboard.getValue()) {
                scoreboard.setClosedOutByAll(true);
            }
        }
    }

    /**
     * Method resets all the scoreboards connected to the PlayerFragment, and sets all the tally
     * marks to their original starting state
     */
    public void resetPlayerFrag() {
        for (Scoreboard scoreboard : scoreboards) {
            scoreboard.setClosedOut(false);
            scoreboard.setClosedOutByAll(false);
            scoreboard.setCount(0);
            scoreboard.getImageView().setImageResource(R.drawable.tally_no_marks);
            if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                game_score_tv.setText("0");
                mTotalScore = 0;
            }
        }
    }

    /**
     * Method removes a mark made by the user.
     * @param scoreValue the integer score value used to find the proper scoreboard
     * @param multiple the incremental value of the mark to be removed from the total count
     */
    public void undoThrow(int scoreValue, int multiple) {
        int previousTotalCount;
        // Find the correct scoreboard
        Scoreboard currentScoreboard = scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)];
        // store the previous count
        previousTotalCount = currentScoreboard.getCount();
        // set a new count
        currentScoreboard.setCount(currentScoreboard.getCount() - multiple);
        // if the count goes below the max number of counts, set the mTallyCount trackers to false,
        // revert the tally mark image, and notify the host activity that the tally mark has been
        // opened
        if ((currentScoreboard.getCount() < Scoreboard.MAX_NUM_OF_TALLY_MARKS)) {
            tallyImageSelector(currentScoreboard);
            currentScoreboard.setClosedOut(false);
            currentScoreboard.setClosedOutByAll(false);
            mCallback.TallyOpened(mTag, scoreValue);
        }

        // if the game mode is standard, and the total score is above zero
        if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                && mTotalScore > 0) {

            // if the current count is over the max number of tally marks then subtract the value
            // from the total score
            if (currentScoreboard.getCount() >= Scoreboard.MAX_NUM_OF_TALLY_MARKS) {
                mTotalScore = mTotalScore - (scoreValue * multiple);
                game_score_tv.setText(String.valueOf(mTotalScore));
                mCallback.TotalScoreHasChanged(mTag, mTotalScore);
            // if the previous mark minus the max number of tallies is greater than zero, and the
            // the increment value is greater than one, subtract only the value of the marks that
            // were overflowed into the total score
            } else if (multiple > 1 && previousTotalCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS > 0) {
                int overflow = previousTotalCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS;
                mTotalScore = mTotalScore - (scoreValue * overflow);
                game_score_tv.setText(String.valueOf(mTotalScore));
                mCallback.TotalScoreHasChanged(mTag, mTotalScore);

            }
        }
    }

    /**
     * Method will set the scoreboard mClosedOutByAll to false
     * @param scoreValue integer of the scoreboard to be changed to false
     */
    public void setClosedOutByAll(int scoreValue, boolean notClosedOut) {
        scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)].setClosedOutByAll(notClosedOut);
    }

    /**
     * Method will return the current state of the scoreboard
     * @param scoreValue integer of the scoreboard to be checked
     * @return current boolean condition of the scoreboard
     */
    public boolean isClosedOut(int scoreValue) {
        return scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)].isClosedOut();
    }

    private void reloadGame(String name, int avatar, boolean[] closedOut, boolean[] allClosedOut, int[] tallyCounts, int totalScore){

        if(name != null){
            name_et.setText(name);
        }

        if(avatar != 0){
            player_avatar_iv.setImageResource(avatar);
        }

        for(int i = 0; i < scoreboards.length; i++){
            scoreboards[i].setClosedOut(closedOut[i]);
            scoreboards[i].setClosedOutByAll(allClosedOut[i]);
            scoreboards[i].setCount(tallyCounts[i]);
            tallyImageSelector(scoreboards[i]);
        }

        if(mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value)) &&
                this.mTotalScore != 0) {
            this.mTotalScore = totalScore;
            game_score_tv.setText(String.valueOf(this.mTotalScore));
        }

    }
}

