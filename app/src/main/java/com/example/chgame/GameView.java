package com.example.chgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isPlayin;
    private int screenX, screenY;
    private float screenRatioX, screenRatioY;
    private Paint paint;
    //create 2 obj (instance) of the background class bc it will help us to make the background move
    private background Background1, Background2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f /screenX;
        screenRatioY=1000f /screenY;

        Background1=new background(screenX, screenY, getResources());
        Background2=new background(screenX, screenY, getResources());

        Background2.x=screenX;

        paint = new Paint();
    }

    @Override
    //methode of Runnable we generated it
    public void run() {
        //this loop is runing when the player is playing
        while (isPlayin){
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
    }

    public void draw(){
        //getholder somthing to put our background on
        if( getHolder().getSurface().isValid()){
            Canvas canvas =getHolder().lockCanvas();
            canvas.drawBitmap(Background1.Background, Background1.x, Background1.y,paint);
            canvas.drawBitmap(Background2.Background, Background2.x, Background2.y,paint);

            //to show the canvas that we want to draw on the screen
            getHolder().unlockCanvasAndPost(canvas);
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
        isPlayin=true;
        thread = new Thread(this);
        //starting this thread we will call run fun then we will give the fuctionality to the run methode after some time
        thread.start();

    }


    // and when this methode is called we will pause our game
    public void pause(){
        //this funtion will just stop the thread if we call it
        try {
            //and isPlaying is set to false when the game pauses
            isPlayin=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
