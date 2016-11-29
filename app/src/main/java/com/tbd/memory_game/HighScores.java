/******************************************************************************
 * file: GameActivity.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 ******************************************************************************/
package com.tbd.memory_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class HighScores extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        //Creates spinner for drop down list of highscores.
        Spinner spinner = (Spinner)findViewById(R.id.hsOptions);
        items = new String[]{"4 Cards", "6 Cards", "8 Cards" , "10 Cards" , "12 Cards" , "14 Cards"
                , "16 Cards" , "18 Cards" , "20 Cards"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        HighScore score= new HighScore(items[0]);
        TextView hs1 = (TextView) findViewById(R.id.hsOne);
        hs1.setText(score.getScore(0));
        TextView hs2 = (TextView) findViewById(R.id.hsTwo);
        hs2.setText(score.getScore(1));
        TextView hs3 = (TextView) findViewById(R.id.hsThree);
        hs3.setText(score.getScore(2));
    }
    //Code to set which highscores are being shown.
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HighScore score= new HighScore(items[position]);
        TextView hs1 = (TextView) findViewById(R.id.hsOne);
        hs1.setText(score.getScore(0));
        TextView hs2 = (TextView) findViewById(R.id.hsTwo);
        hs2.setText(score.getScore(1));
        TextView hs3 = (TextView) findViewById(R.id.hsThree);
        hs3.setText(score.getScore(2));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
