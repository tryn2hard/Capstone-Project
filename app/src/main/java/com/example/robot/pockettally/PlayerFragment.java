package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

    private static final String LOG_TAG = PlayerFragment.class.getSimpleName();

    /**
     * A public interface used to communicate back to the host activity about changes made on the
     * tally marks.
     */
    public interface PlayerProgressListener {

        void PlayerNamed(String tag, String name);

        void AvatarSelected(String tag, int avatar);

        void ScoreboardMarked(String tag, int scoreValue, int multiple);

        void ChangeToScoreboardConditionNotification(String tag, int position, boolean condition);

        void TotalScoreHasChanged(String tag, int totalScore);
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
        Log.i(LOG_TAG, "onAttach called by " + getTag());
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
    public final static String FRAGMENT_ARGS_SCOREBOARD_COUNTS_KEY = "mScoreboardCounts";
    public final static String FRAGMENT_ARGS_TOTAL_SCORE_KEY = "mTotalScore";
    public final static String FRAGMENT_ARGS_GAME_INIT_KEY = "game initialized";
    public final static String FRAGMENT_ARGS_SCOREBOARD_DONE_KEY = "Scoreboard done";
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
    public int[] mScoreboardCounts = new int[7];
    public boolean[] mClosedOut = new boolean[7];
    public boolean[] mScoreboardDone = new boolean[7];
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
        Log.i(LOG_TAG, "onCreateView called by " + getTag());
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

        for (int i = 0; i < mClosedOut.length; i++) {
            mClosedOut[i] = Scoreboard.SCOREBOARD_OPEN;
            mScoreboardCounts[i] = 0;
            mScoreboardDone[i] = Scoreboard.SCOREBOARD_OPEN;
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

            current_Scoreboard_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!current_Scoreboard.isClosedOutByAll()) {
                        if (mVibe) {
                            vibe.vibrate(VIBRATE_TIME);
                        }
                        // If the game is in no points mode and the current scoreboard is not closed out
                        if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value)) && !current_Scoreboard.isClosedOut()) {
                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);

                            // notify the host activity that a mark has been made
                            mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                    Scoreboard.SINGLE_TALLY_MARK);
                            // select the correct image for the tally mark
                            scoreboardImageSelector(current_Scoreboard);


                            // If the game is in standard points mode
                        } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                                && !current_Scoreboard.isClosedOutByAll()) {

                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);

                            if (!current_Scoreboard.isClosedOut()) {
                                // select the proper image to display
                                scoreboardImageSelector(current_Scoreboard);

                            } else {
                                mTotalScore += current_Scoreboard.getValue();
                                game_score_tv.setText(String.valueOf(mTotalScore));
                                mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                            }
                            // notify the host activity that a mark has been made
                            mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                    Scoreboard.SINGLE_TALLY_MARK);

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
                                            // select the correct image for the tally mark
                                            scoreboardImageSelector(current_Scoreboard);

                                            // notify the host activity that a mark has been made
                                            mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                                    Scoreboard.DOUBLE_TALLY_MARK);
                                            // increment the count
                                            current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);

                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                                            && !current_Scoreboard.isClosedOutByAll()) {

                                        // increment the count
                                        current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);

                                        if (!current_Scoreboard.isClosedOut()) {
                                            // select the proper image to display
                                            scoreboardImageSelector(current_Scoreboard);

                                        } else {
                                            mTotalScore += current_Scoreboard.getValue() * Scoreboard.DOUBLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(mTotalScore));
                                            mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                                        }

                                        // notify the host activity that a mark has been made
                                        mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                                Scoreboard.DOUBLE_TALLY_MARK);
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

                                            // select the correct image for the tally mark
                                            scoreboardImageSelector(current_Scoreboard);

                                            // notify the host activity that a mark has been made
                                            mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                                    Scoreboard.TRIPLE_TALLY_MARK);

                                            // increment the count
                                            current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);
                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                                            && !current_Scoreboard.isClosedOutByAll()) {

                                        // increment the count
                                        current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);

                                        if (!current_Scoreboard.isClosedOut()) {
                                            // select the proper image to display
                                            scoreboardImageSelector(current_Scoreboard);
                                        } else {
                                            mTotalScore += current_Scoreboard.getValue() * Scoreboard.TRIPLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(mTotalScore));
                                            mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                                        }

                                        // notify the host activity that a mark has been made
                                        mCallback.ScoreboardMarked(getTag(), current_Scoreboard.getValue(),
                                                Scoreboard.TRIPLE_TALLY_MARK);
                                    }
                                    multiMarkDialog.dismiss();
                                }
                            });
                    multiMarkDialog.show();
                    return false;
                }
            });
        }

        if (mGameInit) {

            mName = getArguments().getString(FRAGMENT_ARGS_PLAYER_NAME_KEY);
            mAvatar = getArguments().getInt(FRAGMENT_ARGS_PLAYER_AVATAR_KEY);
            mClosedOut = getArguments().getBooleanArray(FRAGMENT_ARGS_CLOSED_OUT_KEY);
            mScoreboardCounts = getArguments().getIntArray(FRAGMENT_ARGS_SCOREBOARD_COUNTS_KEY);
            mTotalScore = getArguments().getInt(FRAGMENT_ARGS_TOTAL_SCORE_KEY);
            mScoreboardDone = getArguments().getBooleanArray(FRAGMENT_ARGS_SCOREBOARD_DONE_KEY);

            reloadGame(mName, mAvatar, mClosedOut, mScoreboardDone, mScoreboardCounts, mTotalScore);

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
     *
     * @param current_Scoreboard a instance of the scoreboard class, will be used to retrieve the
     *                           current count
     */
    private void scoreboardImageSelector(Scoreboard current_Scoreboard) {
        Log.i(getTag(), " has called scoreboardImageSelector for " + current_Scoreboard.getValue() + " scoreboard.");
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
                current_Scoreboard.setClosedOut(Scoreboard.SCOREBOARD_CLOSED);
                mCallback.ChangeToScoreboardConditionNotification(
                        getTag(),
                        current_Scoreboard.getValue(),
                        Scoreboard.SCOREBOARD_CLOSED);
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_three_marks);
                break;
            default:
                current_Scoreboard.setClosedOut(Scoreboard.SCOREBOARD_CLOSED);
                current_Scoreboard.getImageView().setImageResource(R.drawable.tally_three_marks);
                mCallback.ChangeToScoreboardConditionNotification(
                        getTag(),
                        current_Scoreboard.getValue(),
                        Scoreboard.SCOREBOARD_CLOSED);
                // if the current game mode is standard, the default should be to add the total points
                // accumulated to the total score
                if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                        && !current_Scoreboard.isClosedOutByAll()) {
                    mTotalScore += (tallyCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS) * current_Scoreboard.getValue();
                    game_score_tv.setText(String.valueOf(mTotalScore));
                    if (!mGameInit) {
                        mCallback.TotalScoreHasChanged(mTag, mTotalScore);
                    }
                }
        }
    }

    /**
     * Method resets all the scoreboards connected to the PlayerFragment, and sets all the tally
     * marks to their original starting state
     */
    public void resetScoreboards() {
        Log.i(LOG_TAG, "resetScoreboards called by " + getTag());
        for (Scoreboard scoreboard : scoreboards) {
            scoreboard.setClosedOut(Scoreboard.SCOREBOARD_OPEN);
            scoreboard.setClosedOutByAll(Scoreboard.SCOREBOARD_OPEN);
            scoreboard.setCount(0);
            scoreboardImageSelector(scoreboard);
        }
        if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
            game_score_tv.setText("0");
            mTotalScore = 0;
        }
    }

    /**
     * Method removes a mark made by the user.
     *
     * @param scoreValue     the integer score value used to find the proper scoreboard
     * @param incrementValue the incremental value of the mark to be removed from the total count
     */
    public void undoThrow(int scoreValue, int incrementValue) {
        Log.i(LOG_TAG, "undoThrow called by " + getTag());

        // Find the correct scoreboard
        Scoreboard currentScoreboard = scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)];

        if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {

            currentScoreboard.setCount(currentScoreboard.getCount() - incrementValue);
            scoreboardImageSelector(currentScoreboard);
            currentScoreboard.setClosedOut(Scoreboard.SCOREBOARD_OPEN);
            currentScoreboard.setClosedOutByAll(Scoreboard.SCOREBOARD_OPEN);
            mCallback.ChangeToScoreboardConditionNotification(
                    mTag,
                    scoreValue,
                    Scoreboard.SCOREBOARD_OPEN);

        } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {

            if (currentScoreboard.getCount() > Scoreboard.MAX_NUM_OF_TALLY_MARKS) {
                if (!(currentScoreboard.getCount() - incrementValue < Scoreboard.MAX_NUM_OF_TALLY_MARKS)) {
                    mTotalScore = mTotalScore - (scoreValue * incrementValue);
                    currentScoreboard.setCount(currentScoreboard.getCount() - incrementValue);
                } else {
                    int overflow = currentScoreboard.getCount() - Scoreboard.MAX_NUM_OF_TALLY_MARKS;
                    if (overflow != 0) {
                        mTotalScore = mTotalScore - (scoreValue * overflow);
                        currentScoreboard.setCount(currentScoreboard.getCount() - incrementValue);
                        scoreboardImageSelector(currentScoreboard);
                        currentScoreboard.setClosedOut(Scoreboard.SCOREBOARD_OPEN);
                        currentScoreboard.setClosedOutByAll(Scoreboard.SCOREBOARD_OPEN);
                        mCallback.ChangeToScoreboardConditionNotification(
                                mTag,
                                scoreValue,
                                Scoreboard.SCOREBOARD_OPEN);
                    }
                }
                game_score_tv.setText(String.valueOf(mTotalScore));
                mCallback.TotalScoreHasChanged(mTag, mTotalScore);
            } else {
                currentScoreboard.setCount(currentScoreboard.getCount() - incrementValue);
                scoreboardImageSelector(currentScoreboard);
                currentScoreboard.setClosedOut(Scoreboard.SCOREBOARD_OPEN);
                currentScoreboard.setClosedOutByAll(Scoreboard.SCOREBOARD_OPEN);
                mCallback.ChangeToScoreboardConditionNotification(
                        mTag,
                        scoreValue,
                        Scoreboard.SCOREBOARD_OPEN);
            }

        }
    }

    /**
     * Method will set the scoreboard mClosedOutByAll to the condition passed in by the host activity
     *
     * @param scoreValue integer of the scoreboard to be changed to false
     * @param condition  boolean value of the state of the scoreboard
     */
    public void setAllClosedOut(int scoreValue, boolean condition) {
        Log.i(LOG_TAG, "setClosedOutByAll called by " + getTag());
        scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)].setClosedOutByAll(condition);
    }

    private void reloadGame(String name, int avatar, boolean[] closedOut, boolean[] scoreboardDone,
                            int[] tallyCounts, int totalScore) {
        Log.i(LOG_TAG, "reloadGame called by " + getTag());

        if (name != null) {
            name_et.setText(name);
        }

        if (avatar != 0) {
            player_avatar_iv.setImageResource(avatar);
        }

        for (int i = 0; i < scoreboards.length; i++) {
            scoreboards[i].setClosedOut(closedOut[i]);
            scoreboards[i].setClosedOutByAll(scoreboardDone[i]);
            scoreboards[i].setCount(tallyCounts[i]);
            scoreboardImageSelector(scoreboards[i]);
        }

        if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value)) &&
                this.mTotalScore != 0) {
            this.mTotalScore = totalScore;
            game_score_tv.setText(String.valueOf(this.mTotalScore));
        }

    }
}

