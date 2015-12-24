package gmastudios.episode7countdown;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;

public class GameLoopThread extends Thread{

    private int FPS = 20;
    private GameView view;
    private boolean running = false;

    public GameLoopThread(GameView view) {
        this.view = view;
        //FPS = view.getResources().getInteger(R.integer.fps);
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    @SuppressLint("WrongCall")
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    if (c != null) {
                        view.onDraw(c);
                    }
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}
