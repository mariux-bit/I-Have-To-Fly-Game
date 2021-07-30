package com.example.chgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class background {
    int x=0,y=0;
    Bitmap Background;

    background(int screenX, int screenY, Resources res){
          Background= BitmapFactory.decodeResource(res, R.drawable.background);
          //now we will resize this background to fit on the screen
        Background= Bitmap.createScaledBitmap(Background, screenX, screenY, false);
    }
}
