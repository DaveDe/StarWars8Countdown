package gmastudios.episode7countdown;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmManagerBR extends BroadcastReceiver {

    private final long COUNTDOWNDATE = 1495774800000l;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        wl.acquire();

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

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.update, finalTime);
       // remoteViews.setImageViewResource(R.id.backgroundImage, R.drawable.stars);
        ComponentName thiswidget = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
        wl.release();
    }
}
