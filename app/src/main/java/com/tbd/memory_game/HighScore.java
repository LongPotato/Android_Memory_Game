package com.tbd.memory_game;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/******************************************************************************
 * file: HighScore.java
 * author: Miraj, Khanh, Adrian
 * class: CS 245 - Programming Graphical User Interface

 * assignment: Android App Project
 * date last modified : 11/18/2016
 *
 * purpose: This class will represent each separate game's top 3 highscores.
 ******************************************************************************/

public class HighScore implements Serializable {

    private int highScore;
    private int allowedHighScores;
    private String[] allHS;
    private String highScoreFileName;
    private File file;
    private File root;

    /* Public,basic constructor. Initalizes HighScore and all variables.
    *
    */
    public HighScore(String fileName){
        highScore=0;
        allowedHighScores=3;
        allHS = new String[allowedHighScores];
        highScoreFileName=fileName+".txt";
        root = new File(Environment.getExternalStorageDirectory(),"Saves");
        if (!root.exists()) {
            root.mkdirs();
        }
        file= new File(root,highScoreFileName);
        createInitialHS();
        importScores();
        Sort();
    }
    /*
    *Creates the HighScores files if it doesn't already exist.
    */
    private void createInitialHS(){
        if(!file.exists()){
            try {
                FileWriter writer= new FileWriter(file);
                writer.append("");
                String placeholderScores = "abc...0";
                for(int i=0;i<allowedHighScores;i++){
                    writer.write(placeholderScores);
                    writer.write(System.getProperty("line.separator"));
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("No highscores");
            }
        }
    }
    /*
    *Meant to be called when the program is called. Saves the contents of allHS to the file.
    */
    public void createExitHS(){
        try {
            FileWriter writer= new FileWriter(file);
            writer.append("");
            for(int i=0;i<allowedHighScores;i++){
                String scores = allHS[i];
                writer.write(scores);
                writer.write(System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("No highscores");
        }
    }
    /*
    *method: changeHighScore
    *purpose: Increment highScore by the pointsChange parameter
    */

    public void changeHighScore(int pointsChange){
        highScore+=pointsChange;
    }

    /*
    *method: setHighScore
    *purpose: Manually set highScore to be the newScore parameter.
    */
    public void setHighScore(int newScore){
        highScore=newScore;
    }
    /*
    *method: getHighScore
    *purpose: Returns highScore
    */
    public int getHighScore(){
        return highScore;
    }
    /*
    *method: addScore
    *purpose:Adds the score to the highscore list and appropriately bumps all other
    *scores down.
    * according to the parameter line to the parameter newScore
    */
    public void addScore(String initial){
        int line=0;
        initial=initial+"..."+highScore;
        for(int place=0;place<allHS.length;place++){
            if (!allHS[place].isEmpty()) {
                String[] separatedScore1 = allHS[place].split("\\.\\.\\.");
                String[] separatedScore2 = initial.split("\\.\\.\\.");
                if(Integer.parseInt(separatedScore1[1]) <= Integer.parseInt(separatedScore2[1])){
                    line = place;
                    break;
                }
            }
        }
        for(int place=line;place<allHS.length;place++){
            String temp =allHS[place];
            allHS[place]=initial;
            initial=temp;
        }
    }
    /*
    *method: getFileScore
    *purpose: Returns the specific highscore on the line
    *according to the parameter
    */
    public String getScore(int line){
        return allHS[line];
    }
    /*
    * Sorts the array allHS into descending order
    */
    public void Sort(){
        String temp="";
        for(int place=1;place<allHS.length;place++) {
            if (!allHS[place].isEmpty()) {
                temp=allHS[place];
                int place2=place-1;
                String[] separatedScore1 = temp.split("\\.\\.\\.");
                String[] separatedScore2 = allHS[place2].split("\\.\\.\\.");
                while(place2>=0 && Integer.parseInt(separatedScore1[1]) > Integer.parseInt(separatedScore2[1])){
                    allHS[place2+1]=allHS[place2];
                    place2-=1;
                    if(place2>=0)
                        separatedScore2 = allHS[place2].split("\\.\\.\\.");
                }
                allHS[place2+1]=temp;
            }
        }
    }
    /*
    *method: importScores
    *purpose: Accesses all highScores on the file and moves them to allHS
    */
    private void importScores(){
        String hs="";
        int counter=0;
        try{
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                hs=reader.nextLine();
                allHS[counter]=hs;
                counter++;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("No such file exists. Try again");
        }
    }
}