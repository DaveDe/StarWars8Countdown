package gmastudios.episode7countdown;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    private final long COUNTDOWNDATE = 1495774800000l;

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, AlarmManagerBR.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBR.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000 * 3, 1000 , pi);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Calendar c = Calendar.getInstance();
            long timeInMillis = c.getTimeInMillis();
            long millisUntil = COUNTDOWNDATE - timeInMillis;
            long days = millisUntil/(1000*60*60*24);
            millisUntil-=(days*(1000*60*60*24));
            long hours = millisUntil/(1000*60*60);
            millisUntil-=(hours*(1000*60*60));
            long minutes = millisUntil/(1000*60);
            millisUntil-=(minutes*(1000*60));
            long seconds = millisUntil/1000;

            String finalTime = Long.toString(days)+ " Days   "+Long.toString(hours)+" Hours \n"
                    + Long.toString(minutes) + " Minutes  " + Long.toString(seconds) + " Seconds";
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.update, finalTime);
           // remoteViews.setImageViewResource(R.id.backgroundImage, R.drawable.stars);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }
}