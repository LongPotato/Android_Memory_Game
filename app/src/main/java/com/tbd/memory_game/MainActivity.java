package com.tbd.memory_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private GameLogic game;
    private static final String GAME = "GAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    //if(game == null) game = new GameLogic(20);
      if(savedInstanceState != null) {
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

    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("save","onRestoreInstanceState()");
        game = (GameLogic) savedInstanceState.getSerializable(GAME);
        String[] choices = game.getGame();
        for(int i = 0; i < choices.length; i++) {
            Log.i("save", choices[i]);
        }
    }*/
}
