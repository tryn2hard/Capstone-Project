package com.example.robot.pockettally;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
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

    @BindView(R.id.closed_out_20)
    View crossOut20;
    @BindView(R.id.closed_out_19)
    View crossOut19;
    @BindView(R.id.closed_out_18)
    View crossOut18;
    @BindView(R.id.closed_out_17)
    View crossOut17;
    @BindView(R.id.closed_out_16)
    View crossOut16;
    @BindView(R.id.closed_out_15)
    View crossOut15;
    @BindView(R.id.closed_out_bulls)
    View crossOutBulls;


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

    private String mCurrentFragTag;
    private String mCurrentName;
    private int mCurrentAvatarID;
    private int mCurrentTallyCount;
    private int mCurrentTotalScore;
    private Boolean mTallyMarkClosed;
    private int mCurrentPlayerID;
    private Player mCurrentPlayer;
    private PlayerFragment mCurrentPlayerFragment;
    private List<Player> Players = new ArrayList<>();

    private FragmentManager mFragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSharedPreferences();

        if(num_of_players == 2) {

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

                Toast.makeText(getApplicationContext(), "End Game Toast", Toast.LENGTH_SHORT).show();

                }

        });

        undo_mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Undo last throw", Toast.LENGTH_SHORT).show();
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

    private void setupPlayerFragments(int playerCount){
        final List<Integer> fragment_containers = new ArrayList<Integer>(){{
            add(R.id.player_1_container);
            add(R.id.player_2_container);
            add(R.id.player_3_container);
            add(R.id.player_4_container);
        }};



        for(int i = 0; i < playerCount; i++){
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

    private String tagGenerator(int value){
        String tag = "Player " + (value + 1);
        return tag;
    }

    private void setupPlayer(int ID, String tag){
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
        } else if (key.equals(getString(R.string.pref_game_mode_key))){
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
        Toast.makeText(this, tag + " has closed number " + scoreValue, Toast.LENGTH_SHORT).show();
        mCurrentPlayerID = getIdFromTag(tag);
        mCurrentPlayer = Players.get(mCurrentPlayerID);
        mCurrentPlayer.tallyMarkClosed(scoreValue);
        if(isTallyMarkClosedOutByAll(scoreValue)){
            markAsClosed(scoreValue);
        }
    }

    private void markAsClosed(int scoreValue){

        switch(scoreValue){
            case 20:
                crossOut20.setVisibility(View.VISIBLE);
                marker_20_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 19:
                crossOut19.setVisibility(View.VISIBLE);
                marker_19_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 18:
                crossOut18.setVisibility(View.VISIBLE);
                marker_18_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 17:
                crossOut17.setVisibility(View.VISIBLE);
                marker_17_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 16:
                crossOut16.setVisibility(View.VISIBLE);
                marker_16_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 15:
                crossOut15.setVisibility(View.VISIBLE);
                marker_15_tv.setTextColor(getResources().getColor(R.color.tally_start));
                changeTallyImageInFragment(scoreValue);
                break;
            case 25:
                crossOutBulls.setVisibility(View.VISIBLE);
                changeTallyImageInFragment(scoreValue);
                break;
        }

    }

    private void changeTallyImageInFragment(int scoreValue) {
        for(int i = 0; i < Players.size(); i++){
            mCurrentPlayer = Players.get(i);
            mCurrentFragTag = mCurrentPlayer.getFragmentTag();
            mCurrentPlayerFragment = (PlayerFragment) mFragmentManager.findFragmentByTag(mCurrentFragTag);
            mCurrentPlayerFragment.tallyMarkClosedOutByAll(scoreValue);
        }
    }

    private boolean isTallyMarkClosedOutByAll(int scoreValue){

        for(int i = 0; i < Players.size(); i++){
                mCurrentPlayer = Players.get(i);
                mTallyMarkClosed = mCurrentPlayer.getTallyMarkCondition(scoreValue);
                if(!mTallyMarkClosed){
                    return false;
                }
        }
        return true;
    }

    @Override
    public void PlayerNamed(String tag, String name) {
        Toast.makeText(this, tag + " has chosen the name " + name, Toast.LENGTH_SHORT).show();
        mCurrentPlayerID = getIdFromTag(tag);
        mCurrentPlayer = Players.get(mCurrentPlayerID);
        mCurrentPlayer.setName(name);
    }

    @Override
    public void AvatarSelected(String tag, int avatar) {
        mCurrentPlayerID = getIdFromTag(tag);
        mCurrentPlayer = Players.get(mCurrentPlayerID);
        mCurrentPlayer.setAvatar(avatar);
    }

    public int getIdFromTag(String tag){
        int id = Integer.parseInt(tag.substring(tag.length() - 1));
        id -= 1;
        return id;
    }
}
