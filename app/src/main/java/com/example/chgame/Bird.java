package com.example.chgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {
    public int speed = 20;//default value of bird speed
    public boolean wasShot = true;
    int x= 0 , y , width, height, birdCounter = 1;
    Bitmap bird1, bird2, bird3, bird4;
    //constructor
    Bird (Resources res) {
        //refer bitmaps to the imgs in drawable
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4);

        width = bird1.getWidth();
        height = bird1.getHeight();

        width /= 6;
        height /= 6;

        /*
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);
         */

        //resizing the bitmaps
        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);

        //initially the bird will be placed off the screen
        y = -height ;

    }
    //sequencing between the four birds to animate their flying wings
    Bitmap getBird () {
        if (birdCounter == 1) {
            birdCounter++;
            return bird1;
        }
        if (birdCounter == 2) {
            birdCounter++;
            return bird2;
        }
        if (birdCounter == 3) {
            birdCounter++;
            return bird3;
        }
        birdCounter = 1;

        return bird4;
    }

    //this function will create a rectangle around the bird
    Rect getCollisionShape () {
        /*
        x: position of the rectangle in the top left corner on X axes
        y: position of the rectangle in the top left corner from Y axes
        x + width: position of the rectangle in the bottom right corner on the X axes
        y + height: position of the rectangle in the bottom right corner on the Y axes
         */
        return new Rect(x, y, x + width, y + height);
    }
}
