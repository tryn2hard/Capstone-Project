package com.example.robot.pockettally;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    private final static int SINGLE_TALLY_MARK = 1;
    private final static int DOUBLE_TALLY_MARK = 2;
    private final static int TRIPLE_TALLY_MARK = 3;


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
                marker20Count = marker20Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker20Count, scoreboard_20_iv);
            }
        });

        scoreboard_20_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker20Count = marker20Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker20Count, scoreboard_20_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker20Count = marker20Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker20Count, scoreboard_20_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_19_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                marker19Count = marker19Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker19Count, scoreboard_19_iv);
            }
        });

        scoreboard_19_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker19Count = marker19Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker19Count, scoreboard_19_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker19Count = marker19Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker19Count, scoreboard_19_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_18_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                marker18Count = marker18Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker18Count, scoreboard_18_iv);
            }
        });

        scoreboard_18_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker18Count = marker18Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker18Count, scoreboard_18_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker18Count = marker18Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker18Count, scoreboard_18_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_17_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                marker17Count = marker17Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker17Count, scoreboard_17_iv);
            }
        });

        scoreboard_17_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker17Count = marker19Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker17Count, scoreboard_17_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker17Count = marker17Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker17Count, scoreboard_17_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_16_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                marker16Count = marker16Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker16Count, scoreboard_16_iv);
            }
        });

        scoreboard_16_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker16Count = marker16Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker16Count, scoreboard_16_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker16Count = marker16Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker16Count, scoreboard_16_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_15_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                marker15Count = marker15Count + SINGLE_TALLY_MARK;
                tallyImageSelector(marker15Count, scoreboard_15_iv);
            }
        });

        scoreboard_15_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker15Count = marker15Count + DOUBLE_TALLY_MARK;
                        tallyImageSelector(marker15Count, scoreboard_15_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker15Count = marker15Count + TRIPLE_TALLY_MARK;
                        tallyImageSelector(marker15Count, scoreboard_15_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
            }

        });

        scoreboard_bulls_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(100);
                markerBullsCount = markerBullsCount + SINGLE_TALLY_MARK;
                tallyImageSelector(markerBullsCount, scoreboard_bulls_iv);
            }
        });

        scoreboard_bulls_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog multiMarkDialog = new Dialog(getActivity());
                multiMarkDialog.setContentView(R.layout.multiple_marks_dialog);
                Button doubleMarkButton = multiMarkDialog.findViewById(R.id.double_mark_button);
                doubleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markerBullsCount = markerBullsCount + DOUBLE_TALLY_MARK;
                        tallyImageSelector(markerBullsCount, scoreboard_bulls_iv);
                        multiMarkDialog.dismiss();
                    }
                });

                Button tripleMarkButton = multiMarkDialog.findViewById(R.id.triple_mark_button);
                tripleMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markerBullsCount = markerBullsCount + TRIPLE_TALLY_MARK;
                        tallyImageSelector(markerBullsCount, scoreboard_bulls_iv);
                        multiMarkDialog.dismiss();
                    }
                });
                multiMarkDialog.show();
                return false;
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

    private void tallyImageSelector(int count, View view) {

        if(count <= MAX_NUM_OF_TALLY_MARKS) {

            switch (count) {
                case SINGLE_TALLY_MARK:
                    view.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.tally_one_mark, null));

                    break;

                case DOUBLE_TALLY_MARK:
                    view.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.tally_two_marks, null));

                    break;

                case TRIPLE_TALLY_MARK:
                    view.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.tally_three_marks, null));

                    break;

            }
        }else{
            view.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.tally_three_marks, null));
            int overflow = count - MAX_NUM_OF_TALLY_MARKS;
            int id = view.getId();
            addScore(overflow, id);

        }

    }

    private void addScore(int overflow, int id){
        switch(id){
            case R.id.scoreboard_20:
                totalScore = totalScore + TALLY_MARK_20_VALUE * overflow;
                marker20Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_19:
                totalScore = totalScore + TALLY_MARK_19_VALUE * overflow;
                marker19Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_18:
                totalScore = totalScore + TALLY_MARK_18_VALUE * overflow;
                marker18Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_17:
                totalScore = totalScore + TALLY_MARK_17_VALUE * overflow;
                marker17Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_16:
                totalScore = totalScore + TALLY_MARK_16_VALUE * overflow;
                marker16Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_15:
                totalScore = totalScore + TALLY_MARK_15_VALUE * overflow;
                marker15Count = MAX_NUM_OF_TALLY_MARKS;
                break;
            case R.id.scoreboard_bulls:
                totalScore = totalScore + TALLY_MARK_BULLS_VALUE * overflow;
                markerBullsCount = MAX_NUM_OF_TALLY_MARKS;
                break;
        }
        String totalScoreString = String.valueOf(totalScore);
        game_score_tv.setText(totalScoreString);
    }


}

