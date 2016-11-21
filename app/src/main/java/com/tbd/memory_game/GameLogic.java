package com.tbd.memory_game;

/**
 * Created by mirajpatel on 11/18/16.
 */

import java.io.Serializable;
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

    public GameLogic(int cardNum){
        this.numOfCards = cardNum;
        game = new String[numOfCards];
        currentGame = new String[numOfCards];
        correct = 0;
        points = 0;
        initializeGame();
        this.score = new HighScore(scoreFile());

    }

    private String scoreFile(){
        return "score" + numOfCards + ".dat" ;
    }

    public void initializeGame(){
        fillCards();
        fillCurrent();

    }

    // tracks current game, initially all cards are filled with 'X'
    private void fillCurrent(){
        for(int i = 0; i < currentGame.length; i++) {
            currentGame[i] = "X";
        }
    }

    // fill game with words and shuffle to randomize position
    private void fillCards(){
        int temp = 0;
        for(int i = 0; i < game.length; i+=2){

            game[i] = choices[temp];
            game[i+1] = choices[temp];
            temp++;
        }

        //Collections.shuffle(Arrays.asList(game));
        Random rnd = new Random();
        for(int i = game.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            String a = game[index];
            game[index] = game[i];
            game[i] = a;

        }
    }

    // takes in two index and returns true if both value is same else false
    public boolean isChoiceCorrect(int index1, int index2){
        if(index1 == index2) return false;

        tries++;
        if (game[index1] == game[index2]) {
            correct+=2;
            points = points + 2;
            score.changeHighScore(2);

            return true;
        }
        else{
            currentGame[index1] = "X";
            currentGame[index2] = "X";
            if(points > 0){
                points = points - 1;
                score.changeHighScore(-1);
            }
            return false;
        }
    }

    public int getPoints(){
        return points;
    }

    // takes in index of user selected card and returns its value
    public String getChoice(int index){
        currentGame[index] = game[index];
        return game[index];
    }

    public boolean isWon(){
        return correct == numOfCards;
    }

    public int getTries(){
        return tries;
    }

    public String[] getCurrentGame(){
        return currentGame;
    }

    public String[] getGame(){
        return game;
    }

    public void reset(){
        numOfCards = 0;
        correct = 0;
        fillCurrent();
    }
}
