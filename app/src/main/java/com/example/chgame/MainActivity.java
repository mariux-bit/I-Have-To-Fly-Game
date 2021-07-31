package com.example.chgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameAcitivity.class));
            }
        });
        TextView highScoreTxt = findViewById(R.id.highscore);

        SharedPreferences prefs =getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: "+prefs.getInt("highscore", 0) );

        isMute= prefs.getBoolean("isMute", false);
        ImageView volumeCtrl = findViewById(R.id.volumCtrl);

        if(isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_24);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_24);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute= !isMute;
                if(isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_24);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_24);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });
    }
}