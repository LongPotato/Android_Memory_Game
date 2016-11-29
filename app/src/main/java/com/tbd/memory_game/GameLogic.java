/******************************************************************************
 * file: GameActivity.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 ******************************************************************************/
package com.tbd.memory_game;

import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic implements Serializable {

    private String[] choices = {"Lion", "Panda", "Koala","Tiger","Rat",
            "Hippo","Bird","Cat","Ant","Dog"};

    private int numOfCards;
    //actual game solution
    private String[] game;
    private int correct;
    private int points;
    private int tries;
    // keep tracks of current game
    private String[] currentGame;
    private HighScore score;

    protected ArrayList<Integer> revealed = new ArrayList<Integer>();
    protected int firstCard = -1;
    protected int secondCard = -1;
    protected boolean tryAgain;
    protected boolean endGame;
    protected boolean lock;

    public GameLogic(int cardNum){
        this.numOfCards = cardNum;
        game = new String[numOfCards];
        currentGame = new String[numOfCards];
        correct = 0;
        points = 0;
        initializeGame();
        this.score = new HighScore(scoreFile());
        endGame=true;
    }
    /*
    * method - scoreFile
    * purpose - returns file name depending on the number of cards
     */
    private String scoreFile(){
        return numOfCards+" Cards" ;
    }

    public void initializeGame(){
        fillCards();
        fillCurrent();

    }

    /*
     * method - fillCurrent
     * purpose - fills initial card game with all 'X' used for testing
     */
    private void fillCurrent(){
        for(int i = 0; i < currentGame.length; i++) {
            currentGame[i] = "X";
        }
    }

    /*
     * method - fillCards
     * purpose -  fills the game with words and randomize it
     */
    private void fillCards(){
        int temp = 0;
        for(int i = 0; i < game.length; i+=2){

            game[i] = choices[temp];
            game[i+1] = choices[temp];
            temp++;
        }

        Random rnd = new Random();
        for(int i = game.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            String a = game[index];
            game[index] = game[i];
            game[i] = a;

        }
    }

    /*
     * method - isChoiceCorrect
     * purpose - recieves indeces of two cards fliped and returns
     * true it the choice match else returns false.
     */
    public boolean isChoiceCorrect(int index1, int index2){
        if(index1 == index2) return false;

        tries++;
        if (game[index1] == game[index2]) {
            correct+=2;
            points = points + 2;
            return true;
        }
        else{
            currentGame[index1] = "X";
            currentGame[index2] = "X";
            if(points > 0){
                points = points - 1;
            }
            return false;
        }
    }

    /*
     * method - getPoints
     * purpose - returns current points used for testing
     */
    public int getPoints(){
        return points;
    }

    /*
     * method - getChoice
     * purpose - takes index input and returns and card at that index
     */
    public String getChoice(int index){
        currentGame[index] = game[index];
        return game[index];
    }

    /*
     * method - isWon
     * purpose - checks if game is won already
     */
    public boolean isWon(){
        return correct == numOfCards;
    }

    /*
     * method getScore
     * purpose - returns score object to caller to modify game score
     */
    public HighScore getScore(){
        return score;
    }

    /*
     * method - getTries
     * purpose - returns number of card flips tried
     */
    public int getTries(){
        return tries;
    }

    /*
     * method - getCurrentGame
     * purpose - returns current game so far
     */
    public String[] getCurrentGame(){
        return currentGame;
    }

    /*
     * method - getGame
     * purpose - returns all the answer for the game
     */
    public String[] getGame(){
        return game;
    }

    /*
     * method - reset
     * purpose - resets the whole game, used for testing
     */
    public void reset(){
        numOfCards = 0;
        correct = 0;
        fillCurrent();
    }
}
