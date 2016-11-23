package com.tbd.memory_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class HighScores extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        HighScore score1= new HighScore("Four Cards");

        Spinner spinner = (Spinner)findViewById(R.id.hsOptions);
        items = new String[]{"Four Cards", "Six Cards", "Eight Cards" , "Ten Cards" , "Twelve Cards" , "Fourteen Cards"
        , "Sixteen Cards" , " Eighteen Cards" , "Twenty Cards"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button mainMenu = (Button) findViewById(R.id.returnButton);
        mainMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent nextScreen = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(nextScreen);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HighScore score= new HighScore(items[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        HighScore score= new HighScore(items[0]);
    }
}
