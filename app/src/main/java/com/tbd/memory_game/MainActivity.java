/******************************************************************************
 * file: GameActivity.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 ******************************************************************************/
package com.tbd.memory_game;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        Button startGame = (Button) findViewById(R.id.startButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(nextScreen);

            }
        });

        Button highScoreScreen = (Button) findViewById(R.id.highScoresButton);
        highScoreScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), HighScores.class);
                startActivity(nextScreen);
            }
        });

        Button setting = (Button) findViewById(R.id.settingButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingScreen = new Intent(getApplicationContext(), Setting.class);
                startActivity(settingScreen);
            }
        });

    }

}
