package com.example.robot.pockettally;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class DartsGameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darts_game);

        if(savedInstanceState == null){

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

            PlayerFragment player1Frag = new PlayerFragment();
            PlayerFragment player2Frag = new PlayerFragment();

            fm.beginTransaction()
                    .add(R.id.player_1_container, player1Frag)
                    .commit();

            fm.beginTransaction()
                    .add(R.id.player_2_container, player2Frag )
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

        if (id == R.id.action_how_to){
            startActivity(new Intent(this, HowToPlayActivity.class));
        }

        if (id == R.id.action_game_settings){}

        if (id == R.id.action_profile_settings){}

        return super.onOptionsItemSelected(item);
    }
}
