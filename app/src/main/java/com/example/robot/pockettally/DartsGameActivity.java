package com.example.robot.pockettally;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DartsGameActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private int num_of_players;
    private String game_mode_string;
    private Boolean pref_vibrate;

    @BindView(R.id.end_game_button)
    Button end_game_button;

    @BindView(R.id.undo_mark_button)
    Button undo_mark_button;

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
                .getString(R.string.pref_num_of_players_key), "2"));
        pref_vibrate = sharedPreferences.getBoolean(getResources()
                .getString(R.string.pref_vibrate_key), getResources().getBoolean(R.bool.pref_vibrate_default));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setupPlayerFragments(int playerCount){
        final List<Integer> fragment_containers = new ArrayList<Integer>(){{
            add(R.id.player_1_container);
            add(R.id.player_2_container);
            add(R.id.player_3_container);
            add(R.id.player_4_container);
        }};

        FragmentManager fm = getSupportFragmentManager();

        for(int i = 0; i < playerCount; i++){
            PlayerFragment playerFragment = new PlayerFragment();
            Bundle args = new Bundle();
            args.putBoolean("vibe", pref_vibrate);
            playerFragment.setArguments(args);
            fm.beginTransaction()
                    .add(fragment_containers.get(i), playerFragment, "PlayerFrag" + i)
                    .commit();
        }

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
            game_mode_string = sharedPreferences.getString(key,
                    getString(R.string.pref_game_mode_default));
        } else if (key.equals(getString(R.string.pref_vibrate_key))) {
            pref_vibrate = sharedPreferences.getBoolean(key,
                    getResources().getBoolean(R.bool.pref_vibrate_default));
        }

        startActivity(new Intent(this, DartsGameActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
