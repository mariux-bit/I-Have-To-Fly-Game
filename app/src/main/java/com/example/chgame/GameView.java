package com.example.chgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    private Random random;
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score=0;
    public static float screenRatioX, screenRatioY;
    private SoundPool soundPool;
    private SharedPreferences prefs;
    private Paint paint;
    private int sound;
    //create an obj of Flight
    private  Flight flight;
    private GameAcitivity activity;
    //creating a list of bullets
    private List<Bullet> bullets;
    //creating an array of birds
    private Bird[] birds;
    //create 2 obj (instance) of the background class bc it will help us to make the background move
    private background Background1, Background2;

    public GameView(GameAcitivity activity, int screenX, int screenY) {
        super(activity);
        this.activity=activity;

        prefs=activity.getSharedPreferences("game",Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes=new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool= new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
         sound= soundPool.load(activity, R.raw.shoot, 1 );

        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f /screenX;
        screenRatioY=1000f /screenY;

        Background1=new background(screenX, screenY, getResources());
        Background2=new background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());
        //initialisation of the bullets list
        bullets = new ArrayList<>();

        Background2.x=screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        //initialization of birds array
        birds = new Bird[4];

        //
        for (int i = 0;i < 4;i++) {
            Bird bird = new Bird(getResources());
            birds[i] = bird;
        }

        random = new Random();
    }

    @Override
    //methode of Runnable we generated it
    public void run() {
        //this loop is runing when the player is playing
        while (isPlaying){
            update ();
            draw ();
            sleep ();
        }

    }

    // creating the methode bellow so we can call them

    //every time this methode is called our background will move
    public void update (){
        // here we'll change the position of our background on the X axes by 10pixels
        Background1.x -=10 *screenRatioX;
        Background2.x -=10* screenRatioX;

        //maybe I have an error here "background"
        if(Background1.x + Background1.Background.getWidth() <0 ){
            Background1.x=screenX;
        }
        if(Background2.x + Background2.Background.getWidth() <0 ){
            Background2.x=screenX;
        }

        if(flight.isGoingUp)
            //moving the plane up by 30
            flight.y -= 30 * screenRatioX ;
        else
            //moving the plane down
            flight.y += 30 * screenRatioX ;

        //making sure the plane won't go off the screen
        if(flight.y < 0)//from the top
            flight.y = 0;
        if(flight.y > screenY - flight.height)//from the bottom
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();
        //for each bullet loop
        for(Bullet bullet : bullets){
            if(bullet.x > screenX)//when the bullet is off screen
                trash.add(bullet);
            //moving the bullet by 50 pixels on the x axes
            bullet.x += 50 * screenRatioX ;

            //running through each of the birds in the bird array
            for (Bird bird : birds) {
                //checking if the bullets hit the bird
                if (Rect.intersects(bird.getCollisionShape(), bullet.getCollisionShape())) {
                    //setting the position of the bird and the bullet to be off the screen
                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;
                }

            }
        }
        for (Bullet bullet : trash){//when the bullets is found in trash we remove it
            bullets.remove(bullet);
        }
        //for each bird loop
        for (Bird bird : birds) {
            //moving birds towards the plane(from right to left)
            bird.x -= bird.speed;

            //checking if the bird is off the screen from the left side
            if (bird.x + bird.width < 0) {
                if (!bird.wasShot) {//ending the game if the bird get to the border left & not get shoot
                    isGameOver = true ;
                    return ;
                }
                //generating a random speed for our bird
                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound);
                //making sure the speed is not less than 10
                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);
                //replacing the bird
                bird.x = screenX;//placing the board at the end of the screen in the right side
                bird.y = random.nextInt(screenY - bird.height);//the position on y axes will be random

                bird.wasShot = false;
            }
            //checking the collision between the bird and the plane
            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true;
                return;
            }

        }

    }

    public void draw(){
        //getholder somthing to put our background on
        if( getHolder().getSurface().isValid()){
            Canvas canvas =getHolder().lockCanvas();
            canvas.drawBitmap(Background1.Background, Background1.x, Background1.y,paint);
            canvas.drawBitmap(Background2.Background, Background2.x, Background2.y,paint);

            //drawing all the birds
            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "", screenX/ 2f, 164, paint);

            //checking if the game is over
            if (isGameOver) {
                isPlaying = false;//breaking the thread
                //drawing the dead plane
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                //a methode we created to garde the high score
                saveIfHighScore();

                waitBeforeExiting();


                return;
            }


            //drawing the plane after drawing the bg
            canvas.drawBitmap(flight.getFlight(), flight.x , flight.y , paint);

            //drawing the bullets
            for(Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y , paint);
            }
            //to show the canvas that we want to draw on the screen
            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore",0)< score){
            SharedPreferences.Editor editor =prefs.edit();
            editor.putInt("highscore",score);
            editor.apply();
        }
    }

    public void sleep (){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //when this methode is called we will resume or start the game
    public void resume (){
        //the variable isPlayin is set to true when the game is resumed
        isPlaying =true;
        thread = new Thread(this);
        //starting this thread we will call run fun then we will give the fuctionality to the run methode after some time
        thread.start();

    }


    // and when this methode is called we will pause our game
    public void pause(){
        //this funtion will just stop the thread if we call it
        try {
            //and isPlaying is set to false when the game pauses
            isPlaying =false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //checking when the user touch the screen
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://if the user tap on the left side of the screen the plane will go up
                if(event.getX() < screenX /2){
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP://if the user tap on the right side of the screen the plane will shoot
                flight.isGoingUp = false;
                if( event.getX() > screenX/2){
                    flight.toShoot ++;
                }
                break;
        }
        return true;
    }

    public void newBullet() {
        if(!prefs.getBoolean("isMute",false))
            soundPool.play(sound, 1, 1, 0, 0, 1);
        Bullet bullet = new Bullet(getResources());
        // placing the bullet near the wings of the plane
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}
