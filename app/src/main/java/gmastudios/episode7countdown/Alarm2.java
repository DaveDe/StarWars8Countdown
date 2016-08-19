package gmastudios.episode7countdown;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;

public class Alarm2 extends BroadcastReceiver {

    private final long ROGUEONE = 1481864400000l;

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c = Calendar.getInstance();
        long timeInMillis = c.getTimeInMillis();
        long millisUntil = ROGUEONE - timeInMillis;
        long days = millisUntil/(1000*60*60*24);
        millisUntil-=(days*(1000*60*60*24));
        long hours = millisUntil/(1000*60*60);
        millisUntil-=(hours*(1000*60*60));
        long minutes = millisUntil/(1000*60);
        millisUntil-=(minutes*(1000*60));
        long seconds = millisUntil/1000;

        String title = "Rogue One\n";

        String finalTime = title + Long.toString(days)+ " Days   "+Long.toString(hours)+" Hours \n"
                + Long.toString(minutes) + " Minutes  " + Long.toString(seconds) + " Seconds";

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.update, finalTime);
        ComponentName thiswidget = new ComponentName(context,RogueOneWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
    }
}
