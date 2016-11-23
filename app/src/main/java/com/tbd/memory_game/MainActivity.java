package com.tbd.memory_game;

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

        Button startGame = (Button) findViewById(R.id.startButton);
        startGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent nextScreen = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(nextScreen);
            }
        });

        Button highScoreScreen = (Button) findViewById(R.id.highScoresButton);
        highScoreScreen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent nextScreen = new Intent(getApplicationContext(),HighScores.class);
                startActivity(nextScreen);
            }
        });

    }
}
