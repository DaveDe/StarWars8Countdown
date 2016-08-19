package gmastudios.episode7countdown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity {

    public static boolean adShown;
    public static final String SHAREDPREFS = "data";

    private final long COUNTDOWNDATE = 1513296000000l;
    private final long ROGUEONE = 1481864400000l;

    private boolean pause;

    private TextView countdown;
    private TextView more;

    private TimerThread thread;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        countdown = (TextView) findViewById(R.id.countdown);
        more = (TextView) findViewById(R.id.more);

        settings = getSharedPreferences(SHAREDPREFS,0);
        editor = settings.edit();

        pause = false;
        adShown = false;

        countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mode = settings.getString("mode","Rogue One");
                if(mode.equals("Rogue One")){
                    editor.putString("mode","Episode 8");
                }else{
                    editor.putString("mode","Rogue One");
                }
                editor.commit();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open link to developer page
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:David DeFazio"));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        thread = new TimerThread();
        thread.execute();

    }

    @Override
    protected void onPause(){
        super.onPause();

        if(!adShown){
            pause = true;
        }

        adShown = false;

    }

    class TimerThread extends AsyncTask<Void,Void,Void>{

        protected Void doInBackground(Void... v) {

            while(true){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTimeLeft();
                    }
                });
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){}
                if(pause){
                    break;
                }
            }
            return null;
        }

        protected void onProgressUpdate(Void... v) {

        }

        protected void onPostExecute(Void... v) {

        }

    }

    private void updateTimeLeft(){

        String mode = settings.getString("mode","Rogue One");

        Calendar c = Calendar.getInstance();
        long timeInMillis = c.getTimeInMillis();
        long millisUntil = 0;
        if(mode.equals("Rogue One")){
            millisUntil = ROGUEONE - timeInMillis;
        }else{
            millisUntil = COUNTDOWNDATE - timeInMillis;
        }
        long days = millisUntil/(1000*60*60*24);
        millisUntil-=(days*(1000*60*60*24));
        long hours = millisUntil/(1000*60*60);
        millisUntil-=(hours*(1000*60*60));
        long minutes = millisUntil/(1000*60);
        millisUntil-=(minutes*(1000*60));
        long seconds = millisUntil/1000;

        countdown.setText(mode + "\n\n" +Long.toString(days)+" Days\n"
                +Long.toString(hours)+" Hours\n"
                +Long.toString(minutes)+" Minutes\n"
                +Long.toString(seconds)+" Seconds\n");
    }

}