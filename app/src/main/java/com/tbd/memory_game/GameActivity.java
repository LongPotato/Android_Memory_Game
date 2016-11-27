/******************************************************************************
 * file: GameActivity.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 ******************************************************************************/
package com.tbd.memory_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private String initial;
    private int numRow, numCol;
    private GameLogic game;
    private HashMap<String, Integer> pic = new HashMap<String, Integer>();
    private HashMap<Integer, Integer> sizeMap = new HashMap<Integer, Integer>();
    private Button tryAgainButton;
    private Button newGameButton;
    private Button endGameButton;
    private TableLayout mainLayout;
    private Button[] holder;
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
        holder=new Button[getSharedPreferences(getString(R.string.preference_file_key), getApplicationContext().MODE_PRIVATE).getInt("SIZE", 4)];

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

        endGameButton = (Button) findViewById(R.id.endGameButton);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!game.isWon()){
                    for(int index=0;index<holder.length;index++){
                        if(holder[index].isEnabled()) {
                            String choice = game.getChoice(holder[index].getId());
                            choice = choice.toLowerCase();
                            holder[index].setBackgroundResource(pic.get(choice));
                            holder[index].setEnabled(false);
                        }
                    }
                }
                else {
                    Toast toast = Toast.makeText(mainLayout.getContext(), "You won!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                game.lock = true;
                tryAgainButton.setEnabled(false);
                game.tryAgain = false;
                endGame();
            }
        });

        newGame(savedInstanceState);

        // create new background music only once when game initially starts.
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

    /*
     * method - onResume
     * purpose - overrides onResume method, used to start background music
     * when screen orientation changes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(bkgrdmsc != null && bkgrdmscBox == 0){
            bkgrdmsc.start();
        }
    }
    /*
     * method - onPause
     * purpose - overrides onPause method, used to pause background music
     * when pressing back or home button, or when screen orientation changes.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(bkgrdmscBox == 0 && bkgrdmsc != null) {
            bkgrdmsc.pause();
            pos = bkgrdmsc.getCurrentPosition();
        }

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
     * End game method. Records score into the highscore class and if it is a new high score, adds
     * it to the appropriate highscore file.
     */
    private void endGame(){
        game.getScore().setHighScore(game.getPoints());
        String[] lScore = game.getScore().getScore(2).split("\\.\\.\\.");
        if (game.getScore().getHighScore() >= Integer.parseInt(lScore[1])){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New High Score!");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    initial = input.getText().toString();
                    game.getScore().addScore(initial);
                    game.getScore().createExitHS();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        endGameButton.setEnabled(false);
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
        endGameButton.setEnabled(true);
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
                holder[index]=button;
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
                holder[index]=button;
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
            endGame();
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

                    firstCard.setEnabled(false);
                    secondCard.setEnabled(false);
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
