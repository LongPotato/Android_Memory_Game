package com.tbd.memory_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private GameLogic game;
    private static final String GAME = "GAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


     /* if(savedInstanceState != null) {
            game = (GameLogic) savedInstanceState.getSerializable(GAME);
          String[] choices = game.getGame();
          for (int i = 0; i < choices.length; i++) {
              Log.i("GAmeLog", choices[i]);
          }
      }
        else {
          game = new GameLogic(20);
          String[] choices = game.getGame();
          for (int i = 0; i < choices.length; i++) {
              Log.i("newLog", choices[i]);
          }
      }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("save","onSaceInstanceState()");
        String[] choices = game.getGame();
        for(int i = 0; i < choices.length; i++) {
            Log.i("save", choices[i]);
        }
        outState.putSerializable(GAME,game);

    }*/


    }

}
