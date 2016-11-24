package com.tbd.memory_game;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private int savedSize = 4;
    private int numRow, numCol;
    private GameLogic game;
    private HashMap<String, Integer> pic = new HashMap<String, Integer>();
    private HashMap<Integer, Integer> sizeMap = new HashMap<Integer, Integer>();
    private Button firstCard;
    private Button secondCard;
    private boolean lock = false;
    private Button tryAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tryAgainButton = (Button) findViewById(R.id.tryAgainButton);
        tryAgainButton.setEnabled(false);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain();
            }
        });

        assignPictures();
        assignSizes();

        // Get saved size from setting
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), getApplicationContext().MODE_PRIVATE);
        savedSize = sharedPref.getInt("SIZE", 4);
        Log.i("info", "saved size is " + savedSize);

        numCol = sizeMap.get(savedSize);
        numRow = savedSize / numCol;

        game = new GameLogic(savedSize);
        loadCards();
    }

    /**
     *
     */
    public void assignPictures() {
        pic.put("ant", R.drawable.ant);
        pic.put("bird", R.drawable.bird);
        pic.put("cat", R.drawable.cat);
        pic.put("dog", R.drawable.dog);
        pic.put("hippo", R.drawable.hippo);
        pic.put("koala", R.drawable.koala);
        pic.put("lion", R.drawable.lion);
        pic.put("rat", R.drawable.rat);
        pic.put("tiger", R.drawable.tiger);
        pic.put("panda", R.drawable.panda);
    }

    /**
     *
     */
    public void assignSizes() {
        sizeMap.put(4, 2);
        sizeMap.put(6, 3);
        sizeMap.put(8, 4);
        sizeMap.put(10, 5);
        sizeMap.put(12, 4);
        sizeMap.put(14, 4);
        sizeMap.put(16, 5);
        sizeMap.put(18, 5);
        sizeMap.put(20, 5);
    }

    /**
     *
     */
    public void loadCards() {
        TableLayout mainLayout = (TableLayout) findViewById(R.id.mainLayout);

        // Adjust table layout for cards display
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        int leftMargin= 0;
        int topMargin= 5;
        int rightMargin= 0;
        int bottomMargin= 20;
        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        int index = 0;
        for (int i = 0; i < numRow; i++) {
            TableRow row = new TableRow(mainLayout.getContext());
            row.setHorizontalGravity(Gravity.CENTER);
            row.setLayoutParams(tableRowParams);

            for (int j = 0; j < numCol; j++) {
                Button button = new Button(mainLayout.getContext());
                button.setBackgroundResource(R.drawable.poker);
                button.setId(index);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flipCard((Button) view);
                    }
                });
                row.addView(button);
                index++;
            }

            mainLayout.addView(row);
        }

        Log.i("info", "index is " + index);

        if (index < savedSize) {
            int leftOver = savedSize - (index);
            TableRow lastRow = new TableRow(mainLayout.getContext());
            lastRow.setHorizontalGravity(Gravity.CENTER);
            lastRow.setLayoutParams(tableRowParams);

            for (int j = 0; j < leftOver; j++) {
                Button button = new Button(mainLayout.getContext());
                button.setBackgroundResource(R.drawable.poker);
                button.setId(index);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flipCard((Button) view);
                    }
                });
                lastRow.addView(button);
                index++;
            }

            mainLayout.addView(lastRow);
        }
    }

    /**
     *
     * @param button
     */
    private void flipCard(Button button) {
        if (!lock) {
            String choice = game.getChoice(button.getId());
            choice = choice.toLowerCase();
            Log.i("info", "id is " + button.getId() + " choice is " + choice);
            button.setBackgroundResource(pic.get(choice));

            if (firstCard == null) {
                Log.i("info", "first card is " + choice);
                firstCard = button;
            } else {
                // Same card
                if (firstCard.getId() == button.getId()) {
                    return;
                }

                secondCard = button;
                Log.i("info", "second card is " + choice);

                if (!game.isChoiceCorrect(firstCard.getId(), secondCard.getId())) {
                    tryAgainButton.setEnabled(true);
                    lock = true;
                } else {
                    firstCard = null;
                    secondCard = null;
                }
            }
        }
    }

    private void tryAgain() {
        firstCard.setBackgroundResource(R.drawable.poker);
        secondCard.setBackgroundResource(R.drawable.poker);
        firstCard = null;
        secondCard = null;
        lock = false;
        tryAgainButton.setEnabled(false);
    }

}
