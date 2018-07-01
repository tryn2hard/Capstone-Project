package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
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

    private static final String LOG_TAG = "PlayerFragment";

    /**
     * A public interface used to communicate back to the host activity about changes made on the
     * tally marks.
     */
    public interface PlayerProgressListener {
        void TallyClosed(String tag, int position);

        void PlayerNamed(String tag, String name);

        void AvatarSelected(String tag, int avatar);

        void TallyMarked(String tag, int scoreValue, int multiple);

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

    // an array of scoreboards to hold the data for each of the tally marks.
    private Scoreboard[] scoreboards = new Scoreboard[7];

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

    private int totalScore = 0;
    private Vibrator vibe;
    private final static int VIBRATE_TIME = 100;

    private String mGameMode;
    private Boolean mVibe;
    private String mName;
    private int mAvatar;

    private String mTag;


    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, rootView);

        mTag = getTag();

        // Retrieval of the arguments sent from the host activity
        if (getArguments() != null) {
            mVibe = getArguments().getBoolean(FRAGMENT_ARGS_VIBE_KEY);
            mGameMode = getArguments().getString(FRAGMENT_ARGS_GAME_MODE_KEY);
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
            final Scoreboard current_Scoreboard = scoreboards[i]; // get current scoreboard
            final ImageView current_Scoreboard_iv = current_Scoreboard.getImageView(); // get current scoreboard's ImageView

            current_Scoreboard_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!current_Scoreboard.ismClosedOutByAll()) {
                        if (mVibe) {
                            vibe.vibrate(VIBRATE_TIME);
                        }
                        // If the game is in no points mode and the current scoreboard is not closed out
                        if (mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value)) && !current_Scoreboard.isClosedOut()) {
                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);
                            // notify the host activity that a mark has been made
                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.SINGLE_TALLY_MARK);
                            // select the correct image for the tally mark
                            tallyImageSelector(current_Scoreboard);
                        // If the game is in standard points mode
                        } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                            // notify the host activity that a mark has been made
                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.SINGLE_TALLY_MARK);
                            // increment the count
                            current_Scoreboard.incrementCount(Scoreboard.SINGLE_TALLY_MARK);
                            // if the tally mark hasn't been closed out then select the image to display,
                            // else add the current mark to the total score and notify the host activity that
                            // the total score has changed
                            if (!current_Scoreboard.isClosedOut()) {
                                tallyImageSelector(current_Scoreboard);
                            } else {
                                totalScore += current_Scoreboard.getValue();
                                game_score_tv.setText(String.valueOf(totalScore));
                                mCallback.TotalScoreHasChanged(mTag, totalScore);
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
                                            current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);
                                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.DOUBLE_TALLY_MARK);
                                            tallyImageSelector(current_Scoreboard);
                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                                        mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.DOUBLE_TALLY_MARK);
                                        current_Scoreboard.incrementCount(Scoreboard.DOUBLE_TALLY_MARK);
                                        if (!current_Scoreboard.isClosedOut()) {
                                            tallyImageSelector(current_Scoreboard);
                                        } else {
                                            totalScore += current_Scoreboard.getValue() * Scoreboard.DOUBLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(totalScore));
                                            mCallback.TotalScoreHasChanged(mTag, totalScore);
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
                                            current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);
                                            mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.TRIPLE_TALLY_MARK);
                                            tallyImageSelector(current_Scoreboard);
                                        }
                                    } else if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                                        mCallback.TallyMarked(getTag(), current_Scoreboard.getValue(), Scoreboard.TRIPLE_TALLY_MARK);
                                        current_Scoreboard.incrementCount(Scoreboard.TRIPLE_TALLY_MARK);
                                        if (!current_Scoreboard.isClosedOut()) {
                                            tallyImageSelector(current_Scoreboard);
                                        } else {
                                            totalScore += current_Scoreboard.getValue() * Scoreboard.TRIPLE_TALLY_MARK;
                                            game_score_tv.setText(String.valueOf(totalScore));
                                            mCallback.TotalScoreHasChanged(mTag, totalScore);
                                        }
                                    }
                                    multiMarkDialog.dismiss();
                                }
                            });
                    multiMarkDialog.show();
                    return false;
                }
            });
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
                        && !current_Scoreboard.ismClosedOutByAll()) {
                    totalScore += (tallyCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS) * current_Scoreboard.getValue();
                    game_score_tv.setText(String.valueOf(totalScore));
                    mCallback.TotalScoreHasChanged(mTag, totalScore);
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
                scoreboard.setmClosedOutByAll(true);
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
            scoreboard.setmClosedOutByAll(false);
            scoreboard.setCount(0);
            scoreboard.getImageView().setImageResource(R.drawable.tally_no_marks);
            if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))) {
                game_score_tv.setText("0");
                totalScore = 0;
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
        // if the count goes below the max number of counts, set the closedOut trackers to false,
        // revert the tally mark image, and notify the host activity that the tally mark has been
        // opened
        if ((currentScoreboard.getCount() < Scoreboard.MAX_NUM_OF_TALLY_MARKS)) {
            tallyImageSelector(currentScoreboard);
            currentScoreboard.setClosedOut(false);
            currentScoreboard.setmClosedOutByAll(false);
            mCallback.TallyOpened(mTag, scoreValue);
        }

        // if the game mode is standard, and the total score is above zero
        if (mGameMode.equals(getResources().getString(R.string.pref_standard_game_mode_value))
                && totalScore > 0) {

            // if the current count is over the max number of tally marks then subtract the value
            // from the total score
            if (currentScoreboard.getCount() >= Scoreboard.MAX_NUM_OF_TALLY_MARKS) {
                totalScore = totalScore - (scoreValue * multiple);
                game_score_tv.setText(String.valueOf(totalScore));
                mCallback.TotalScoreHasChanged(mTag, totalScore);
            // if the previous mark minus the max number of tallies is greater than zero, and the
            // the increment value is greater than one, subtract only the value of the marks that
            // were overflowed into the total score
            } else if (multiple > 1 && previousTotalCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS > 0) {
                int overflow = previousTotalCount - Scoreboard.MAX_NUM_OF_TALLY_MARKS;
                totalScore = totalScore - (scoreValue * overflow);
                game_score_tv.setText(String.valueOf(totalScore));
                mCallback.TotalScoreHasChanged(mTag, totalScore);

            }
        }
    }

    /**
     * Method will set the scoreboard mClosedOutByAll to false
     * @param scoreValue integer of the scoreboard to be changed to false
     */
    public void setClosedOutByAll(int scoreValue, boolean notClosedOut) {
        scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)].setmClosedOutByAll(notClosedOut);
    }

    /**
     * Method will return the current state of the scoreboard
     * @param scoreValue integer of the scoreboard to be checked
     * @return current boolean condition of the scoreboard
     */
    public boolean isClosedOut(int scoreValue) {
        return scoreboards[ScoreboardUtils.matchScoreValue(scoreValue)].isClosedOut();
    }
}

