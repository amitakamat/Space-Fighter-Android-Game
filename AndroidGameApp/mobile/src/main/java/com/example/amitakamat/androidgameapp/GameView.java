package com.example.amitakamat.androidgameapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Color;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy enemies;
    private Friend friend;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private Boom boom;
    int screenX;
    int countMisses;
    int wrongHits;
    int enemyHit;
    static int level;
    boolean flag;
    private boolean isGameOver;
    int score;
    int highScore[] = new int[4];
    SharedPreferences sharedPreferences;
    Context context;

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s  = new Star(screenX, screenY);
            stars.add(s);
        }
        enemies = new Enemy(context, screenX, screenY);
        friend = new Friend(context, screenX, screenY);
        boom = new Boom(context);
        this.screenX = screenX;
        countMisses = 0;
        enemyHit = 0;
        wrongHits = 0;
        level = 1;
        isGameOver = false;
        score = 0;
        this.context = context;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

    }

    @Override
    public void run() {
        while (playing) {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }


    private void update() {
        score+= level;
        player.update();
        boom.setX(-450);
        boom.setY(-450);

        for (Star s : stars) {
            s.update(player.getSpeed());
        }
        if(enemies.getX()==screenX){
            flag = true;
        }
        enemies.update(player.getSpeed());
        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            enemies.setX(-400);
            enemyHit++;
            if(enemyHit%5 == 0){
                level++;
            }
        }
        else{
            //if the enemy has just entered
            if(flag){
                //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()){
                    //increment countMisses
                    countMisses++;

                    //setting the flag false so that the else part is executed only when new enemy enters the screen
                    flag = false;
                    //if no of Misses is equal to 5, then game is over.
                    if(countMisses==5){
                        //setting playing false to stop the game.
                        playing = false;
                        isGameOver = true;
                        for(int i=0;i<4;i++){
                            if(highScore[i]<score){

                                final int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for(int i=0;i<4;i++){
                            int j = i+1;
                            e.putInt("score"+j,highScore[i]);
                        }
                        e.apply();
                    }
                }
            }
        }
        friend.update(player.getSpeed());
        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            //displaying the boom at the collision
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            wrongHits++;
            friend.setX(-400);
            if(wrongHits == 3) {
                playing = false;
                isGameOver = true;
                for (int i = 0; i < 4; i++) {
                    if (highScore[i] < score) {

                        final int finalI = i;
                        highScore[i] = score;
                        break;
                    }
                }

                SharedPreferences.Editor e = sharedPreferences.edit();
                for (int i = 0; i < 4; i++) {
                    int j = i + 1;
                    e.putInt("score" + j, highScore[i]);
                }
                e.apply();
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            //drawing all stars
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.drawText("Level:"+level,100,100,paint);

            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);


            canvas.drawBitmap(
                    enemies.getBitmap(),
                    enemies.getX(),
                    enemies.getY(),
                    paint
            );


            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            canvas.drawBitmap(

                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );

            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }
}
