package gmastudios.episode7countdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemies {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private GameView gameView;
    private Bitmap bmp;
    private Random rand = new Random();
    private int r = rand.nextInt(2)+1;//1 negative, 2 positive

    public Enemies(GameView gameView, Bitmap bmp, int y) {
        this.gameView=gameView;
        this.bmp=bmp;
        this.y = y;
        this.x = gameView.getResources().getInteger(R.integer.enemy_x_init);
        this.xSpeed = gameView.getResources().getInteger(R.integer.enemy_x_speed);
        this.ySpeed = gameView.getResources().getInteger(R.integer.enemy_y_speed);
    }

    private void update() {
        if(r == 1) {
            x += (xSpeed*-1);
        }else{
            x += xSpeed;
        }
        y += ySpeed;
        if(x > gameView.getWidth() - bmp.getWidth() - 5){
            xSpeed *= -1;
        }
        if(x <= 10){
            xSpeed *= -1;
        }
        if(y > (gameView.getHeight()*2)){//+200
            y = -bmp.getHeight();
            Random r = new Random();
            x = r.nextInt(gameView.getWidth()-(gameView.getWidth()/5));
        }
        for(int i = 0; i < gameView.getBullet().length; i++){//check if enemy is hit
                if((gameView.getBulletIndex(i).getYCollisionVal() <= y+bmp.getHeight())&&
                        (gameView.getBulletIndex(i).getYCollisionVal()+gameView.getBulletIndex(i).getBmp().getHeight() >= y)&&
                        (x <= gameView.getBulletIndex(i).getXCollisionVal())&&
                        (x + bmp.getWidth() >= gameView.getBulletIndex(i).getXCollisionVal()))
                {
                    gameView.getSp().play(gameView.getSoundID2(), 1, 1, 0, 0, 1);
                    y += (gameView.getHeight()+bmp.getHeight()+5);//enemy shifts down the length of a screen when hit
                    gameView.getBulletIndex(i).setYCollisionVal(-10000);
                    gameView.getBulletIndex(i).setYCollisionVal(-10000);
                    gameView.setScore(gameView.getScore()+1);
                }
        }
        if(y >= gameView.getHeight()-10 && y <= gameView.getHeight() + 10){//game over
            gameView.setAd(gameView.getAd()+1);
            gameView.setGameOver(true);
        }
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x , y, null);
    }
    public int getX(){
        return x;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public int getY(){
        return y;
    }
}