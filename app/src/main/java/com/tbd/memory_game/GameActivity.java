/******************************************************************************
 * file: GameActivity.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 ******************************************************************************/
package com.tbd.memory_game;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game play activity class, display the UI and make request to game logic.
 */
public class GameActivity extends AppCompatActivity {
    private int savedSize = 4;
    private int numRow, numCol;
    private GameLogic game;
    private HashMap<String, Integer> pic = new HashMap<String, Integer>();
    private HashMap<Integer, Integer> sizeMap = new HashMap<Integer, Integer>();
    private Button tryAgainButton;
    private Button newGameButton;
    private TableLayout mainLayout;
    private Button firstCard, secondCard;
    private static final String GAME = "GAME";
    private TextView scoreDisplay;

    static MediaPlayer bkgrdmsc;
    private int bkgrdmscBox ;//= Setting.mbkgrdCheked;
    private static boolean isOn;
    static int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mainLayout = (TableLayout) findViewById(R.id.mainLayout);
        scoreDisplay = (TextView) findViewById(R.id.scoreDisplay);

        tryAgainButton = (Button) findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain();
            }
        });

        // New game
        newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(mainLayout.getContext(), "Created new game", Toast.LENGTH_SHORT);
                toast.show();
                mainLayout.removeAllViews();
                newGame(null);
                firstCard = null;
                secondCard = null;
            }
        });

        newGame(savedInstanceState);

        /*if(bkgrdmscBox == 0) {
            bkgrdmsc = MediaPlayer.create(GameActivity.this, R.raw.background);
            bkgrdmsc.setLooping(true);
            bkgrdmsc.start();
        }//*/
        bkgrdmscBox = Setting.mbkgrdCheked;
        if(bkgrdmscBox == 0) {
            if(bkgrdmsc == null && !isOn) {
                bkgrdmsc = MediaPlayer.create(GameActivity.this, R.raw.background);
                bkgrdmsc.setLooping(true);
                bkgrdmsc.start();
                isOn = true;
            }//*/
            else{
                bkgrdmsc.seekTo(pos);
            }
        }//*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bkgrdmsc != null && bkgrdmscBox == 0){
            bkgrdmsc.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(bkgrdmscBox == 0 && bkgrdmsc != null) {
            //bkgrdmsc.release();
            bkgrdmsc.pause();
        }
       // Setting.mbkgrdCheked = 0;
        pos = bkgrdmsc.getCurrentPosition();
       // finish();
    }
    /**
     * Save game logic instance when the screen is rotated.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("save","onSaceInstanceState()");
        outState.putSerializable(GAME, game);
    }

    /**
     * Initialize elements, creating new game.
     * Restore saved instance.
     */
    public void newGame(Bundle savedInstanceState) {
        assignPictures();
        assignSizes();

        // Get saved size from setting
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), getApplicationContext().MODE_PRIVATE);
        savedSize = sharedPref.getInt("SIZE", 4);
        Log.i("info", "saved size is " + savedSize);

        numCol = sizeMap.get(savedSize);
        numRow = savedSize / numCol;

        // Save instance state for screen rotation
        if(savedInstanceState != null) {
            game = (GameLogic) savedInstanceState.getSerializable(GAME);
        }
        else {
            // New game
            game = new GameLogic(savedSize);
            game.tryAgain = false;
        }

        tryAgainButton.setEnabled(game.tryAgain);
        scoreDisplay.setText("Score: " + game.getPoints());
        loadCards();
    }

    /**
     * Assign pictures to the corresponding animal name.
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
     * Assign the size of the row (number of column)
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
     * Load the cards onto the mainLayout, calculate the number of rows and columns.
     * For restored state: check if the card has been revealed before.
     * Keep track of first and second card.
     */
    public void loadCards() {
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

                if (game.revealed.contains(index)) {
                    Log.i("reveal", "index is " + index + " choice is " + game.getChoice(index));
                    button.setBackgroundResource(pic.get(game.getChoice(index).toLowerCase()));
                } else {
                    button.setBackgroundResource(R.drawable.poker);
                }

                if (game.firstCard == index) {
                    firstCard = button;
                }

                if (game.secondCard == index) {
                    secondCard = button;
                }

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

                if (game.revealed.contains(index)) {
                    Log.i("reveal", "index is " + index + " choice is " + game.getChoice(index));
                    button.setBackgroundResource(pic.get(game.getChoice(index).toLowerCase()));
                } else {
                    button.setBackgroundResource(R.drawable.poker);
                }

                if (game.firstCard == index) {
                    firstCard = button;
                }

                if (game.secondCard == index) {
                    secondCard = button;
                }

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
     * Change the image of the button card.
     * Check for win condition and card selection order.
     * @param button
     */
    private void flipCard(Button button) {
        if (game.isWon()) {
            // Game over
            Toast toast = Toast.makeText(mainLayout.getContext(), "You won!", Toast.LENGTH_SHORT);
            toast.show();
            game.lock = true;
            tryAgainButton.setEnabled(false);
            game.tryAgain = false;
        }

        if (!game.lock) {
            String choice = game.getChoice(button.getId());
            choice = choice.toLowerCase();
            Log.i("info", "id is " + button.getId() + " choice is " + choice);
            button.setBackgroundResource(pic.get(choice));

            if (firstCard == null) {
                Log.i("info", "first card is " + choice);
                firstCard = button;
                game.firstCard = button.getId();
                game.revealed.add(button.getId());
            } else {
                game.tryAgain = false;

                // Same card
                if (firstCard.getId() == button.getId()) {
                    return;
                }

                secondCard = button;
                game.secondCard = button.getId();
                game.revealed.add(button.getId());
                Log.i("info", "second card is " + choice);

                if (!game.isChoiceCorrect(firstCard.getId(), secondCard.getId())) {
                    tryAgainButton.setEnabled(true);
                    game.tryAgain = true;
                    game.lock = true;
                    scoreDisplay.setText("Score: " + game.getPoints());
                } else {
                    firstCard = null;
                    secondCard = null;
                    game.firstCard = -1;
                    game.secondCard = -1;
                    scoreDisplay.setText("Score: " + game.getPoints());
                }
            }
        }
    }

    /**
     * When both card are not match, flip them back, and reset the statuses.
     */
    private void tryAgain() {
        firstCard.setBackgroundResource(R.drawable.poker);
        secondCard.setBackgroundResource(R.drawable.poker);
        game.revealed.remove(game.revealed.indexOf(firstCard.getId()));
        game.revealed.remove(game.revealed.indexOf(secondCard.getId()));
        firstCard = null;
        secondCard = null;
        game.firstCard = -1;
        game.secondCard = -1;
        game.lock = false;
        tryAgainButton.setEnabled(false);
        game.tryAgain = false;
    }

}
