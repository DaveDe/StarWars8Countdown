package gmastudios.episode7countdown;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameView extends SurfaceView {

    private Bitmap bmp;
    private Bitmap bmp2;
    private Bitmap bmp3;
    private Bitmap bmp4;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private int x;//for hero
    private Paint p;
    private Enemies[] enemies;
    private Hero h;
    private Bullet[] bullets;
    private int score;
    private boolean gameOver;
    private SoundPool sp;
    private int soundID;
    private int soundID2;
    InterstitialAd mInterstitialAd;
    private int ad;
    private int scaledFontSize;
    private int bulletTimer;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){

        int numBullets = getResources().getInteger(R.integer.num_bullets);
        int numEnemies = getResources().getInteger(R.integer.num_enemies);

        enemies = new Enemies[numEnemies];
        bullets = new Bullet[numBullets];
        p = new Paint();
        gameOver = false;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-8421459443129126/5581188891");
        requestNewInterstitial();
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = sp.load(context, R.raw.laser_sound, 1);
        soundID2 = sp.load(context, R.raw.explosion, 1);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                while (retry) {
                    try {
                        gameLoopThread.setRunning(false);
                        gameLoopThread.join();
                        ((Activity) getContext()).finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    retry = false;
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        scaledFontSize = getResources().getDimensionPixelSize(R.dimen.fontSize);
        p.setTextSize(scaledFontSize * 2);
        p.setColor(Color.parseColor("#ffd700"));
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.fighter);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.stars);
        bmp3 = BitmapFactory.decodeResource(getResources(), R.drawable.tie_fighter);
        bmp4 = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
        int eSpacingFactor = getResources().getInteger(R.integer.enemy_spacing_factor);
        for(int i = 0; i < enemies.length; i++){
            enemies[i] = new Enemies(this,bmp3,i*eSpacingFactor);
        }
        h = new Hero(this,bmp);
        int bSpacingFactor = getResources().getInteger(R.integer.bullet_spacing_factor);
        for(int i = 0; i < bullets.length; i++){
            bullets[i] = new Bullet(this, bmp4, -150, h.getY() + (bSpacingFactor*i));//x is at arbitrary value off the screen
        }
        ad = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        bulletTimer++;
        double height = getHeight();
        double width = getWidth();

        if (x >= width - bmp.getWidth()-1) {
            x = (int)width - bmp.getWidth()-5;
        }
        if (x <= 0) {
            x = 5;
        }
        canvas.drawBitmap(bmp2, 0, 0, null);
        canvas.drawText(Integer.toString(score), scaledFontSize, scaledFontSize*2, p);

        h.onDraw(canvas);
        if(!gameOver){
            for(int i = 0; i < enemies.length; i++){
                enemies[i].onDraw(canvas);
            }
            for(int i = 0; i < bullets.length; i++){
                bullets[i].onDraw(canvas);
            }
        }else {
            canvas.drawText("Game Over", scaledFontSize*3, scaledFontSize*7, p);
            try{
                int highscore = Integer.parseInt(StaticMethods.readFirstLine("highscore.txt",getContext()));
                canvas.drawText("Highscore: " + Integer.toString(highscore), scaledFontSize*2,  scaledFontSize*20, p);
            }catch(IOException e){}
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!gameOver){
            x = (int)event.getX()-bmp.getWidth()/2;
            h.setX(x);
        }else{
            if (mInterstitialAd.isLoaded() && (ad%3==0)) {
                mInterstitialAd.show();
            }else {
                requestNewInterstitial();
                restart();
            }
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    restart();
                }
            });
        }
        return true;
    }
    private void restart(){
        try{
            Thread.sleep(2000);
            gameOver = false;
            int eSpacingFactor = getResources().getInteger(R.integer.enemy_spacing_factor);
            for (int i = 0; i < enemies.length; i++) {
                enemies[i] = new Enemies(this, bmp3, i * eSpacingFactor);
            }
            h = new Hero(this, bmp);
            int bSpacingFactor = getResources().getInteger(R.integer.bullet_spacing_factor);
            for (int i = 0; i < bullets.length; i++) {
                bullets[i] = new Bullet(this, bmp4, -150, h.getY() + (bSpacingFactor*i));//x is at arbitrary value off the screen
            }
            int highScore = Integer.parseInt(StaticMethods.readFirstLine("highscore.txt",getContext()));
            if(score > highScore){
                StaticMethods.write("highscore.txt",Integer.toString(score),getContext());
            }
            score = 0;
        }catch(InterruptedException e){}
        catch (IOException io){}
    }
    public Hero getH(){
        return h;
    }
    public Enemies[] getEnemies(){
        return enemies;
    }
    public Enemies getEnemiesIndex(int i){
        return enemies[i];
    }
    public Bullet[] getBullet(){
        return bullets;
    }
    public Bullet getBulletIndex(int i){
        return bullets[i];
    }
    public void setScore(int score){
        this.score = score;
    }
    public int getScore(){
        return score;
    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public SoundPool getSp(){
        return sp;
    }
    public int getSoundID(){
        return soundID;
    }
    public int getSoundID2(){
        return soundID2;
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4A5BC2497191112F02A42DC7DBDFEA47")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    public void setAd(int ad){
        this.ad = ad;
    }
    public int getAd(){
        return ad;
    }
}
