package com.example.chgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.TimeUnit;

public class background {
    int x=0,y=0  ;
    Bitmap bg1, bg2, bg3, bg4;

    background(int screenX, int screenY, Resources res){
        bg1= BitmapFactory.decodeResource(res, R.drawable.bg1);
        bg2= BitmapFactory.decodeResource(res, R.drawable.bg2);
        bg3= BitmapFactory.decodeResource(res, R.drawable.bg3);
        bg4= BitmapFactory.decodeResource(res, R.drawable.bg4);

        //now we will resize this background to fit on the screen
        bg1= Bitmap.createScaledBitmap(bg1, screenX, screenY, false);
        bg2= Bitmap.createScaledBitmap(bg2, screenX, screenY, false);
        bg3= Bitmap.createScaledBitmap(bg3, screenX, screenY, false);
        bg4= Bitmap.createScaledBitmap(bg4, screenX, screenY, false);
    }


    Bitmap getBgSunRise(){
        return bg1;
    }
    Bitmap getBgDay(){
        return bg2;
    }
    Bitmap getBgCloud(){
        return bg3;
    }
    Bitmap getBgNight(){
        return bg4;
    }


}
