package com.tbd.memory_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HighScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        HighScore score1= new HighScore("fourCards");
        HighScore score2= new HighScore("sixCards");
        HighScore score3= new HighScore("eightCards");
        HighScore score4= new HighScore("tenCards");
        HighScore score5= new HighScore("twelveCards");
        HighScore score6= new HighScore("fourteenCards");
        HighScore score7= new HighScore("sixteenCards");
        HighScore score8= new HighScore("eighteenCards");
        HighScore score9= new HighScore("twentyCards");

        Button mainMenu = (Button) findViewById(R.id.returnButton);
        mainMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent nextScreen = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(nextScreen);
            }
        });
    }
}
