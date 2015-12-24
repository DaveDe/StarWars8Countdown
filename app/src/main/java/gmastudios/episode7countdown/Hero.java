package gmastudios.episode7countdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hero {
    private int x = 50;
    private int y;
    private GameView gameView;
    private Bitmap bmp;

    public Hero(GameView gameView, Bitmap bmp) {
        this.gameView=gameView;
        this.bmp=bmp;
    }

    private void update() {
        y = gameView.getHeight()-bmp.getHeight()-5;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x , y, null);
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public Bitmap getBmp(){
        return bmp;
    }
    public int getY(){
        return y;
    }
}
