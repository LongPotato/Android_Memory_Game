package com.tbd.memory_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TableLayout mainLayout = (TableLayout) findViewById(R.id.mainLayout);

        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin= 0;
        int topMargin= 5;
        int rightMargin= 0;
        int bottomMargin= 20;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        int index = 0;
        for (int i = 0; i < 4; i++) {
            TableRow row = new TableRow(mainLayout.getContext());
            row.setHorizontalGravity(Gravity.CENTER);
            row.setLayoutParams(tableRowParams);

            for (int j = 0; j < 5; j++) {
                Button button = new Button(mainLayout.getContext());
                button.setBackgroundResource(R.drawable.lion);
                button.setId(index);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                row.addView(button);
                index++;
            }
            mainLayout.addView(row);
        }
    }
}
