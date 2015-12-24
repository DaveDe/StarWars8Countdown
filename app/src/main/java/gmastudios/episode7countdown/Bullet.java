package gmastudios.episode7countdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet {
    private int x;
    private int y;
    private int bulletSpeed;
    private GameView gameView;
    private Bitmap bmp;
    private boolean a;
    private int collisionYVal = -10000;
    private int collisionXVal = -10000;

    public Bullet(GameView gameView, Bitmap bmp, int x, int y){
        this.gameView=gameView;
        this.bmp=bmp;
        this.x = x;
        this.y = y;
        a = false;
        this.bulletSpeed = gameView.getResources().getInteger(R.integer.bullet_speed);

    }

    private void update() {
        if(a){//initialize bullet position
            gameView.getSp().play(gameView.getSoundID(), 1, 1, 0, 0, 1);
            y = gameView.getHeight() - gameView.getH().getBmp().getHeight() - (int)(bmp.getHeight()/1.5);
            x = gameView.getH().getX() + (gameView.getH().getBmp().getWidth()/2)-5;
            a = false;
        }
        if(y <= gameView.getHeight()) {
            y -= bulletSpeed;
        }
        for(int i = 0; i < gameView.getEnemies().length; i++){
            if((x >= gameView.getEnemiesIndex(i).getX()) && (x <= gameView.getEnemiesIndex(i).getX() + gameView.getEnemiesIndex(i).getBmp().getWidth())
                    &&(y >= gameView.getEnemiesIndex(i).getY())&&(y <= gameView.getEnemiesIndex(i).getY() + gameView.getEnemiesIndex(i).getBmp().getHeight())
                    &&(y >= 0)){//bullet hits enemy
                collisionYVal = y;
                collisionXVal = x;
                x = -100;
            }
        }
        if(y <= -bmp.getHeight()){
            a = true;
        }
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x , y, null);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Bitmap getBmp(){
        return bmp;
    }
    public int getYCollisionVal(){
        return collisionYVal;
    }
    public void setYCollisionVal(int val){
        this.collisionYVal = val;
    }
    public int getXCollisionVal(){
        return collisionXVal;
    }
    public void setXCollisionVal(int val){
        this.collisionXVal = val;
    }
    public void setY(int y){
        this.y = y;
    }
}
