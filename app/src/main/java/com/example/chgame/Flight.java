package com.example.chgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.chgame.GameView.screenRatioX;
import static com.example.chgame.GameView.screenRatioY;

public class Flight {
    public boolean isGoingUp = false;
    public int toShoot =0 ;
    int x, y, width , height , wingCounter = 0 , shootCounter = 1;
    Bitmap flight1, flight2 , shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    GameView gameView;
    Flight(GameView gameView, int screenY, Resources res) {
        this.gameView = gameView;
        //importing 2 planes images to animate flying
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);
        //both images have the same dimension
        width = flight1.getWidth();
        height = flight1.getHeight();

        //reducing the size
        width /= 4;
        height /= 4;
        //making the width and height compatible in other devices
        /* "i comment it because it generate an error on condor"
        width *= (int) screenRatioX;
        height *= (int) screenRatioY;
        */
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        //referencing bitmap shoot objs to the imgs in drawable
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);
        //resizing imgs
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2 ; //position middle of the screen
        x = (int)(64 * screenRatioX); //margin of displaying the plane
    }

    //sequencing between the two images
    Bitmap getFlight(){
        if(toShoot != 0){
            if(shootCounter == 1){
                shootCounter++;
                return shoot1;
            }
            if(shootCounter == 2){
                shootCounter++;
                return shoot2;
            }
            if(shootCounter == 3){
                shootCounter++;
                return shoot3;
            }
            if(shootCounter == 4){
                shootCounter++;
                return shoot4;
            }
            shootCounter = 1;
            toShoot--;
            gameView.newBullet();
            return shoot5;

        }
        if(wingCounter == 0 ){
            wingCounter++;
            return flight1 ;
        }

        wingCounter--;
        return flight2;
    }
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }


    public Bitmap getDead() {
        return dead;
    }
}


