package com.tbd.memory_game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by Khanh on 11/23/16.
 */

public class Setting extends AppCompatActivity {
    public static int mbkgrdCheked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Spinner s = (Spinner) findViewById(R.id.selectSize);

        // Get data from the size array to set the values for the spinner picker
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        // Get saved size to set the initial spinner value
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), getApplicationContext().MODE_PRIVATE);
        int savedSize = sharedPref.getInt("SIZE", 4);
        Log.i("info", "saved size in setting is " + savedSize);
        int spinnerPosition = adapter.getPosition(Integer.toString(savedSize));
        s.setSelection(spinnerPosition);

        s.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                int select;

                switch (position) {
                    case 0:
                        select = 4;
                        break;
                    case 1:
                        select = 6;
                        break;
                    case 2:
                        select = 8;
                        break;
                    case 3:
                        select = 10;
                        break;
                    case 4:
                        select = 12;
                        break;
                    case 5:
                        select = 14;
                        break;
                    case 6:
                        select = 16;
                        break;
                    case 7:
                        select = 18;
                        break;
                    case 8:
                        select = 20;
                        break;
                    default:
                        select = 4;
                        break;
                }
                // Save preferred size
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("SIZE", select);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        bkgrdMusicOff();
    }

    private void bkgrdMusicOff() {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setChecked(getFromSp("checkBox1"));
        mbkgrdCheked = checkBox1.isChecked() ? 1 : 0;
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    mbkgrdCheked = 1;
                } else {
                    mbkgrdCheked = 0;
                }
                saveInSp("checkBox1",b);
            }
        });
    }

    private void saveInSp(String key, boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.edu.cpp.tbd.memory", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private boolean getFromSp(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.edu.cpp.tbd.memory", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }







}
