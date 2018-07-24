package com.example.robot.pockettally;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class Widget_Provider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String winner, int avatarId, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pocket_tally_widget);

        // Create an Intent to launch DartsGameActivity when clicked
        Intent intent = new Intent(context, DartsGameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_avatar_image, pendingIntent);

        views.setImageViewResource(R.id.widget_avatar_image, avatarId);
        views.setTextViewText(R.id.widget_player_name_tv, winner);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        UpdateWidgetService.displayNewWinner(context);
    }

    public static void updateWinnerWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String winner, int avatarId, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, winner, avatarId, appWidgetId);
        }
    }

}

