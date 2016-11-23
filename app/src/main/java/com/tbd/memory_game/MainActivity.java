package com.tbd.memory_game;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private GameLogic game;
    private static final String GAME = "GAME";

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
