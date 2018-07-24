package com.example.robot.pockettally;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateWidgetService extends IntentService {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPlayerDatabaseReference;
    private ChildEventListener mChildEventListener;

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
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mPlayerDatabaseReference = mFirebaseDatabase.getReference().child("winners");


            }
        }
    }

    private void handleActionDisplayNewWinner(String winner, int avatarId){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, Widget_Provider.class));

        Widget_Provider.updateWinnerWidgets(this, appWidgetManager, winner, avatarId, appWidgetIds);
    }
}
