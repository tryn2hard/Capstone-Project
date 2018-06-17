package com.example.robot.pockettally;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class HowToPlayActivity extends AppCompatActivity {


    @BindViews({ R.id.game_objective_header, R.id.cricket_numbers_header, R.id.game_start_header})
    List<TextView> headerViews;

    @BindViews({ R.id.game_objective_body, R.id.cricket_numbers_body, R.id.game_start_body})
    List<TextView> bodyViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.how_to_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.toolbar_menu:
                if (headerViews.get(0).getTextSize() <
                        getResources().getDimension(R.dimen.large_font_header)) {

                    for(int i = 0; i < bodyViews.size(); i++){
                        bodyViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                getResources().getDimension(R.dimen.large_font_body));
                    }

                    for(int i = 0; i < headerViews.size(); i++){
                        headerViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.large_font_header));
                    }
                } else {
                    for(int i = 0; i < headerViews.size(); i++) {
                        headerViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                getResources().getDimension(R.dimen.med_font_header));
                    }

                        for(int i = 0; i < headerViews.size(); i++){
                            bodyViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                    getResources().getDimension(R.dimen.med_font_body));
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
