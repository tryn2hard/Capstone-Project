package com.example.robot.pockettally;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class DartsGameActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        PlayerFragment.PlayerProgressListener {


    private int num_of_players;
    private String game_mode;
    private Boolean pref_vibrate;

    @BindView(R.id.end_game_button)
    Button end_game_button;

    @BindView(R.id.undo_mark_button)
    Button undo_mark_button;

    @BindViews({R.id.closed_out_20, R.id.closed_out_19, R.id.closed_out_18,
            R.id.closed_out_17, R.id.closed_out_16, R.id.closed_out_15, R.id.closed_out_bulls})
    List<View> CrossedOutDividers;

    @BindView(R.id.marker_20_tv)
    TextView marker_20_tv;
    @BindView(R.id.marker_19_tv)
    TextView marker_19_tv;
    @BindView(R.id.marker_18_tv)
    TextView marker_18_tv;
    @BindView(R.id.marker_17_tv)
    TextView marker_17_tv;
    @BindView(R.id.marker_16_tv)
    TextView marker_16_tv;
    @BindView(R.id.marker_15_tv)
    TextView marker_15_tv;
    @BindView(R.id.marker_bulls_iv)
    ImageView marker_bulls_iv;

    private List<Player> Players = new ArrayList<>();
    private ArrayList<GameMark> GameHistory = new ArrayList<>();

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSharedPreferences();

        if (num_of_players == 0) {
            num_of_players = 2;
        }

        if (num_of_players == 2) {

            setContentView(R.layout.activity_darts_game);

        } else {
            setContentView(R.layout.activity_darts_game_multi_player);
        }

        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();

        setupPlayerFragments(num_of_players);

        end_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetGame();

            }

        });

        undo_mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Undo last throw", Toast.LENGTH_SHORT).show();
                undoThrow();
            }
        });

    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        num_of_players = Integer.parseInt(sharedPreferences.getString(getResources()
                .getString(R.string.pref_num_of_players_key), getResources().getString(R.string.pref_num_of_players_default)));
        pref_vibrate = sharedPreferences.getBoolean(getResources()
                .getString(R.string.pref_vibrate_key), getResources().getBoolean(R.bool.pref_vibrate_default));
        game_mode = sharedPreferences.getString(getResources().getString(R.string.pref_game_mode_key), getResources().getString(R.string.pref_game_mode_default));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setupPlayerFragments(int playerCount) {
        final List<Integer> fragment_containers = new ArrayList<Integer>() {{
            add(R.id.player_1_container);
            add(R.id.player_2_container);
            add(R.id.player_3_container);
            add(R.id.player_4_container);
        }};

        for (int i = 0; i < playerCount; i++) {
            PlayerFragment playerFragment = new PlayerFragment();
            String tag = tagGenerator(i);
            setupPlayer(i, tag);
            Bundle args = new Bundle();
            args.putBoolean(PlayerFragment.FRAGMENT_ARGS_VIBE_KEY, pref_vibrate);
            args.putString(PlayerFragment.FRAGMENT_ARGS_GAME_MODE_KEY, game_mode);
            playerFragment.setArguments(args);
            mFragmentManager.beginTransaction()
                    .add(fragment_containers.get(i), playerFragment, tag)
                    .commit();
        }

    }

    private String tagGenerator(int value) {
        return "Player " + (value + 1);
    }

    private void setupPlayer(int ID, String tag) {
        Players.add(new Player(ID, tag));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_how_to) {
            startActivity(new Intent(this, HowToPlayActivity.class));
        }
        if (id == R.id.action_game_settings) {
            startActivity(new Intent(this, GameSettingsActivity.class));
            return true;
        }
        if (id == R.id.action_profile_settings) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_num_of_players_key))) {
            num_of_players = Integer.parseInt(sharedPreferences.getString(key,
                    getString(R.string.pref_num_of_players_default)));
        } else if (key.equals(getString(R.string.pref_game_mode_key))) {
            game_mode = sharedPreferences.getString(key,
                    getString(R.string.pref_game_mode_default));
        } else if (key.equals(getString(R.string.pref_vibrate_key))) {
            pref_vibrate = sharedPreferences.getBoolean(key,
                    getResources().getBoolean(R.bool.pref_vibrate_default));
        }

        // Not sure I like that DartsGameActivity gets started up anytime a preference changes
        startActivity(new Intent(this, DartsGameActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void TallyClosed(String tag, int scoreValue) {
//        Toast.makeText(this, tag + " has closed number " + scoreValue, Toast.LENGTH_SHORT).show();
        Players.get(getIdFromTag(tag)).tallyMarkClosed(scoreValue);
        if (isTallyMarkClosedOutByAll(scoreValue)) {
            tallyMarkClosedOutByAll(scoreValue, Players.get(getIdFromTag(tag)));
        }
        if (hasPlayerClosedOutEverything(tag)) {
            showEndOfGameDialog(tag);
        }
    }

    private void tallyMarkClosedOutByAll(int scoreValue, Player currentPlayer) {

        for (int i = 0; i < currentPlayer.getScoreValues().length; i++) {
            if (currentPlayer.getScoreValues()[i] == scoreValue) {
                CrossedOutDividers.get(i).setVisibility(View.VISIBLE);
                changeTallyImageInFragment(scoreValue);
            }
        }
    }

    private void changeTallyImageInFragment(int scoreValue) {
        for(int i = 0; i < Players.size(); i++) {
            PlayerFragment frag =
                    (PlayerFragment) mFragmentManager.findFragmentByTag(Players.get(i).getFragmentTag());
            frag.tallyMarkClosedOutByAll(scoreValue);
        }
    }

    private boolean isTallyMarkClosedOutByAll(int scoreValue) {

        for (int i = 0; i < Players.size(); i++) {
            boolean isTallyMarkClosed = Players.get(i).getTallyMarkCondition(scoreValue);
            if (!isTallyMarkClosed) {
                return false;
            }
        }
        return true;
    }

    private void showEndOfGameDialog(String tag) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.end_of_game_dialog);
        TextView winner_tv = dialog.findViewById(R.id.winning_player_tv);
        String winner;
        if (Players.get(getIdFromTag(tag)).getName() != null) {
            winner = Players.get(getIdFromTag(tag)).getName() + " has won!";
        } else {
            winner = tag + " has won!";
        }
        winner_tv.setText(winner);

        Button play_again_button = dialog.findViewById(R.id.play_again_button);
        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void resetGame() {
        Toast.makeText(this, "game has been started again", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < Players.size(); i++) {
            Players.get(i).resetPlayer();
            PlayerFragment frag =
                    (PlayerFragment) mFragmentManager.findFragmentByTag(Players.get(i).getFragmentTag());
            frag.resetPlayerFrag();
        }

        for (int j = 0; j < CrossedOutDividers.size(); j++) {
            CrossedOutDividers.get(j).setVisibility(View.INVISIBLE);
        }

        GameHistory.clear();
    }

    private boolean hasPlayerClosedOutEverything(String tag) {

        return Players.get(getIdFromTag(tag)).checkAllTallyMarks();
    }

    private void isPlayerLeadingInPoints() {

    }

    private void storeGameHistory(String tag, int scoreValue, int multiple) {
        GameHistory.add(new GameMark(tag, scoreValue, multiple));
    }

    private GameMark getGameHistory() {
        GameMark undoMark = null;

        if (!GameHistory.isEmpty()) {
            undoMark = GameHistory.get(GameHistory.size() - 1);
            GameHistory.remove(GameHistory.size() - 1);
            GameHistory.trimToSize();
        }
        return undoMark;
    }

    @Override
    public void PlayerNamed(String tag, String name) {
        Players.get(getIdFromTag(tag)).setName(name);
        Toast.makeText(this, tag + " has chosen the name " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void AvatarSelected(String tag, int avatar) {
        Players.get(getIdFromTag(tag)).setAvatar(avatar);
    }

    @Override
    public void TallyMarked(String tag, int scoreValue, int multiple) {
        storeGameHistory(tag, scoreValue, multiple);
    }

    public int getIdFromTag(String tag) {
        int id = Integer.parseInt(tag.substring(tag.length() - 1));
        id -= 1;
        return id;
    }

    public void undoThrow() {
        GameMark undoMark = getGameHistory();
        if (undoMark == null) {
            Toast.makeText(this, "Nothing to Undo", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("undoThrow", undoMark.getmTag() + " " + undoMark.getmValue() + " " + undoMark.getmMarkMultiple());
            PlayerFragment currentFrag = (PlayerFragment) mFragmentManager.findFragmentByTag(undoMark.getmTag());
            currentFrag.undoThrow(undoMark.getmValue(), undoMark.getmMarkMultiple());
            Player currentPlayer = Players.get(getIdFromTag(undoMark.getmTag()));
            if (currentPlayer.getTallyMarkCondition(undoMark.getmValue())) {
                if (isTallyMarkClosedOutByAll(undoMark.getmValue())) {
                    for (int i = 0; i < currentPlayer.getScoreValues().length; i++) {
                        if (currentPlayer.getScoreValues()[i] == undoMark.getmValue()) {
                            CrossedOutDividers.get(i).setVisibility(View.INVISIBLE);
                            currentPlayer.tallyMarkUnClosed(undoMark.getmValue());
                        }
                    }
                } else {
                    currentPlayer.tallyMarkUnClosed(undoMark.getmValue());
                }
            }

        }
    }
}


