package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {
    PlayerProgressListener mCallback;

    public interface PlayerProgressListener {
        public void TallyClosed(String tag, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (PlayerProgressListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            + " must implement PlayerProgressListener");
        }
    }

    // Constants
    private final static int TALLY_MARK_20_VALUE = 20;
    private final static int TALLY_MARK_19_VALUE = 19;
    private final static int TALLY_MARK_18_VALUE = 18;
    private final static int TALLY_MARK_17_VALUE = 17;
    private final static int TALLY_MARK_16_VALUE = 16;
    private final static int TALLY_MARK_15_VALUE = 15;
    private final static int TALLY_MARK_BULLS_VALUE = 25;

    private final static int MAX_NUM_OF_TALLY_MARKS = 3;
    private final static int SINGLE_TALLY_MARK = 1;
    private final static int DOUBLE_TALLY_MARK = 2;
    private final static int TRIPLE_TALLY_MARK = 3;

    public final static String FRAGMENT_ARGS_GAME_MODE_KEY = "game mode";
    public final static String FRAGMENT_ARGS_VIBE_KEY = "vibe";

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
    @BindView(R.id.game_score)
    TextView game_score_tv;
    @BindView(R.id.player_avatar_image)
    ImageView player_avatar_iv;
    @BindView(R.id.divider4)
    View divider_v;
    @BindView(R.id.editText)
    EditText name_et;

    private List<Integer> avatars;

    private int totalScore = 0;
    private Vibrator vibe;
    private final static int VIBRATE_TIME = 100;

    private String mGameMode;
    private Boolean mVibe;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mVibe = getArguments().getBoolean(FRAGMENT_ARGS_VIBE_KEY);
            mGameMode = getArguments().getString(FRAGMENT_ARGS_GAME_MODE_KEY);
        }

        if(mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))){
            game_score_tv.setVisibility(View.INVISIBLE);
            divider_v.setVisibility(View.INVISIBLE);

        }

        name_et.setText(getTag());

        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        avatars = new AvatarImageAssets().getAvatars();

        // Initialize Scoreboards
        scoreboards[0] = new Scoreboard(TALLY_MARK_20_VALUE, scoreboard_20_iv);
        scoreboards[1] = new Scoreboard(TALLY_MARK_19_VALUE, scoreboard_19_iv);
        scoreboards[2] = new Scoreboard(TALLY_MARK_18_VALUE, scoreboard_18_iv);
        scoreboards[3] = new Scoreboard(TALLY_MARK_17_VALUE, scoreboard_17_iv);
        scoreboards[4] = new Scoreboard(TALLY_MARK_16_VALUE, scoreboard_16_iv);
        scoreboards[5] = new Scoreboard(TALLY_MARK_15_VALUE, scoreboard_15_iv);
        scoreboards[6] = new Scoreboard(TALLY_MARK_BULLS_VALUE, scoreboard_bulls_iv);

        // Set listeners on each Scoreboard
        for (int i = 0; i < scoreboards.length; i++) {
            final Scoreboard current_Scoreboard = scoreboards[i]; // get current scoreboard
            final ImageView current_Scoreboard_iv = current_Scoreboard.getImageView(); // get current scoreboard's ImageView

            current_Scoreboard_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mVibe) {
                        vibe.vibrate(VIBRATE_TIME);
                    }
                    current_Scoreboard.incrementCount(SINGLE_TALLY_MARK);
                    // If this Scoreboard has not yet been closed out, select tally image;
                    // else only update totalScore and game_score_tv
                    if (!current_Scoreboard.isClosedOut()) {
                        tallyImageSelector(current_Scoreboard);
                    } else if(!mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))){
                        game_score_tv.setText(String.valueOf(totalScore += current_Scoreboard.getValue()));
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
                            if(mVibe) {
                                vibe.vibrate(VIBRATE_TIME);
                            }
                            current_Scoreboard.incrementCount(DOUBLE_TALLY_MARK);
                            if (!current_Scoreboard.isClosedOut()) {
                                tallyImageSelector(current_Scoreboard);
                            } else if(!mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))){
                                game_score_tv.setText(String.valueOf(totalScore += current_Scoreboard.getValue() * DOUBLE_TALLY_MARK));
                            }
                            multiMarkDialog.dismiss();
                        }
                    });

                    multiMarkDialog.findViewById(R.id.triple_mark_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mVibe) {
                                vibe.vibrate(VIBRATE_TIME);
                            }
                            current_Scoreboard.incrementCount(TRIPLE_TALLY_MARK);
                            if (!current_Scoreboard.isClosedOut()) {
                                tallyImageSelector(current_Scoreboard);
                            } else if(!mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))){
                                game_score_tv.setText(String.valueOf(totalScore += current_Scoreboard.getValue() * TRIPLE_TALLY_MARK));
                            }
                            multiMarkDialog.dismiss();
                        }
                    });
                    multiMarkDialog.show();
                    return false;
                }
            });
        }

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.avatar_dialog);
        final List<Integer> avatars_iv = new ArrayList<Integer>(){{
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

        for(int i = 0; i < avatars_iv.size(); i++){
            ImageView currentImageView = dialog.findViewById(avatars_iv.get(i));
            final int currentIndex = i;
            currentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player_avatar_iv.setImageResource(avatars.get(currentIndex));
                    dialog.dismiss();
                }
            });
        }


        player_avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });
        return rootView;
    }


    private void tallyImageSelector(Scoreboard current_Scoreboard) {
        int tallyCount = current_Scoreboard.getCount();
        switch (current_Scoreboard.getCount()) {
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
                if(!mGameMode.equals(getResources().getString(R.string.pref_no_points_game_mode_value))) {
                    totalScore += (tallyCount - MAX_NUM_OF_TALLY_MARKS) * current_Scoreboard.getValue();
                    game_score_tv.setText(String.valueOf(totalScore));
                }
        }
    }
}