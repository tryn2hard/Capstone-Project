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
public class PocketTallyWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile =
            "com.example.robot.pockettally.widget";

    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(
                mSharedPrefFile, 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pocket_tally_widget);
        views.setTextViewText(R.id.appwidget_id_label, String.valueOf(appWidgetId));

        views.setTextViewText(R.id.appwidget_update,
                context.getResources().getString(
                        R.string.date_count_format, count, dateString));

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();

        Intent intentUpdate = new Intent(context, PocketTallyWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[]{appWidgetId};

        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

