package gmastudios.episode7countdown;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    private final long COUNTDOWNDATE = 1495774800000l;

    private boolean pause;

    private TextView countdown;
    private TextView more;
    private TimerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        countdown = (TextView) findViewById(R.id.countdown);
        more = (TextView) findViewById(R.id.more);

        pause = false;

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open link to Addon
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=add.on.dave.sumrun"));
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

        pause = true;

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

        countdown.setText(Long.toString(days)+" Days\n"
                +Long.toString(hours)+" Hours\n"
                +Long.toString(minutes)+" Minutes\n"
                +Long.toString(seconds)+" Seconds\n");
    }

}