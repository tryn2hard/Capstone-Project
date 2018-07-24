package com.example.robot.pockettally;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class UpdateWidgetService extends IntentService {

    public static final String ACTION_DISPLAY_NEW_WINNER = "com.example.robot.pockettally.action.display_new_winner";

    public UpdateWidgetService(String name) {
        super(name);
    }

    public static void displayNewWinner(Context context){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_DISPLAY_NEW_WINNER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_DISPLAY_NEW_WINNER.equals(action)){
                String winner = intent.getStringExtra("winner");
                int avatarId = intent.getIntExtra("avatarId", 0);
                handleActionDisplayNewWinner(winner, avatarId);
            }
        }
    }

    private void handleActionDisplayNewWinner(String winner, int avatarId){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, Widget_Provider.class));

    }
}
