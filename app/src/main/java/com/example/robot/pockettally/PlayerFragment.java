package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    // Constants

    private final static int TALLY_MARK_20_VALUE = 20;
    private final static int TALLY_MARK_19_VALUE = 19;
    private final static int TALLY_MARK_18_VALUE = 18;
    private final static int TALLY_MARK_17_VALUE = 17;
    private final static int TALLY_MARK_16_VALUE = 16;
    private final static int TALLY_MARK_15_VALUE = 15;
    private final static int TALLY_MARK_BULLS_VALUE = 25;
    private final static int MAX_NUM_OF_TALLY_MARKS = 3;


    // Binding the image views
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


    //Counters for each tally mark
    private int marker20Count = 0;
    private int marker19Count = 0;
    private int marker18Count = 0;
    private int marker17Count = 0;
    private int marker16Count = 0;
    private int marker15Count = 0;
    private int markerBullsCount = 0;


    private int totalScore = 0;
    private Vibrator vibe;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, rootView);

       vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


        scoreboard_20_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker20Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker20Count = tallyImageSelector(marker20Count, view);
                }
                else {
                    addScore(TALLY_MARK_20_VALUE);
                }
            }
        });

        scoreboard_19_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker19Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker19Count = tallyImageSelector(marker19Count, view);
                }
                else {
                    addScore(TALLY_MARK_19_VALUE);
                }
            }
        });

        scoreboard_18_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker18Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker18Count = tallyImageSelector(marker18Count, view);
                }
                else {
                    addScore(TALLY_MARK_18_VALUE);
                }
            }
        });

        scoreboard_17_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker17Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker17Count = tallyImageSelector(marker17Count, view);
                }
                else {
                    addScore(TALLY_MARK_17_VALUE);
                }
            }
        });

        scoreboard_16_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker16Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker16Count = tallyImageSelector(marker16Count, view);
                }
                else {
                    addScore(TALLY_MARK_16_VALUE);
                }
            }
        });

        scoreboard_15_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(marker15Count < MAX_NUM_OF_TALLY_MARKS) {
                    marker15Count = tallyImageSelector(marker15Count, view);
                }
                else {
                    addScore(TALLY_MARK_15_VALUE);
                }
            }
        });

        scoreboard_bulls_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                if(markerBullsCount < MAX_NUM_OF_TALLY_MARKS) {
                    markerBullsCount = tallyImageSelector(markerBullsCount, view);
                }
                else {
                    addScore(TALLY_MARK_BULLS_VALUE);
                }
            }
        });

        player_avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.avatar_dialog);

                ImageView avatar_man = (ImageView) dialog.findViewById(R.id.avatar_man);
                avatar_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man1 = (ImageView) dialog.findViewById(R.id.avatar_man_1);
                avatar_man1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_1);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man2 = (ImageView) dialog.findViewById(R.id.avatar_man_2);
                avatar_man2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_2);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man3 = (ImageView) dialog.findViewById(R.id.avatar_man_3);
                avatar_man3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_3);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man4 = (ImageView) dialog.findViewById(R.id.avatar_man_4);
                avatar_man4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_4);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man5 = (ImageView) dialog.findViewById(R.id.avatar_man_5);
                avatar_man5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_5);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_man6 = (ImageView) dialog.findViewById(R.id.avatar_man_6);
                avatar_man6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.man_6);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_woman = (ImageView) dialog.findViewById(R.id.avatar_woman);
                avatar_woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.woman);
                        dialog.dismiss();
                    }
                });

                ImageView avatar_woman1 = (ImageView) dialog.findViewById(R.id.avatar_woman_1);
                avatar_woman1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player_avatar_iv.setImageResource(R.drawable.woman_1);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private int tallyImageSelector(int count, View view) {

        switch (count) {
            case 0:
                view.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.tally_one_mark, null));
                count++;
                break;

            case 1:
                view.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.tally_two_marks, null));
                count++;
                break;

            case 2:
                view.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.tally_three_marks, null));
                count++;
                break;

        }
        return count;
    }

    private void addScore(int value){
        totalScore = totalScore + value;
        String totalScoreString = String.valueOf(totalScore);
        game_score_tv.setText(totalScoreString);
    }

}

