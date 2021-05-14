package sample;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;


public class Smash implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me;
    private OpponentAPI opponent;
    private Board board;
    private UserInterfaceAPI info;
    private DictionaryAPI dictionary;
    private int turnCount;

    private ArrayList<String> storeWords;

    Smash(PlayerAPI me, OpponentAPI opponent, Board board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
        turnCount = 0;
        storeWords = new ArrayList<String>();
    }

    public String getCommand() {
        // Add your code here to input your commands
        // Your code must give the command NAME <botname> at the start of the game
        String command = "";
        switch (turnCount) {
            case 0:
                command = "NAME Smash";
                break;
            case 1:
                command = "PASS";
                break;
            case 2:
                command = "HELP";
                break;
            case 3:
                command = "SCORE";
                break;
            case 4:
                command = "POOL";
                break;
            default:
                // call the function that looks at the empty spaces on the board
                ArrayList<String> emptySpaces = new ArrayList<String>();
                emptySpaces = findEmptySpaces();


                // then call the function that returns an array list of all possible Words to put on the board
                ArrayList<Word> possibleWords = new ArrayList<Word>();
                possibleWords = findPossibleWords(emptySpaces);
                System.out.println(possibleWords);


                // Word chosenWord = maximisePoint(possibleWords);

                // Chose one random word in the list
                int size = possibleWords.size();
                Random rand = new Random();
                int index = rand.nextInt(size);
                System.out.println("chosen word = "+possibleWords.get(index));

                // get the command that corresponds to this word
                command = getWordCommand(possibleWords.get(index));

                break;
        }
        turnCount++;
        return command;
    }

    // This method tests the points of all the possible words, and returns the word with the most points
    public Word maximisePoint(ArrayList<Word> wordsList) {
        ArrayList<Integer> pointsList = new ArrayList<Integer>();
        // Compute the points for each possible word
        for (Word word : wordsList) {
            int points = board.getWordPoints(word);
            pointsList.add(points);
        }

        // Choose the word with the max points
        int max = Collections.max(pointsList);
        int indexMax = pointsList.indexOf(max);

        return wordsList.get(indexMax);
    }

    public String getWordCommand(Word word) {
        String command="";
        int column;
        int row;
        String direction;
        String rowLetter;
        //Get position of the first square


        // across or down

        if (word.isHorizontal()) {
            column = word.getFirstColumn();
            row = word.getRow();
            direction ="A";
        } else {
            column = word.getFirstColumn();
            row = word.getRow();
            direction ="D";
        }

        rowLetter = Character.toString((char) (row+64));

        command = (rowLetter+1)+ String.valueOf(column+1) + " " + direction + " " + word.getLetters();

        return command;
    }

    public ArrayList<String> findEmptySpaces() {
        ArrayList<String> emptySpaces = new ArrayList<String>();

        // checking for across positions
        for(int row = 0; row<15; row++) {
            for(int column = 0; column<15; column++) {
                if (board.getSquare(row, column).isOccupied() && !board.getSquare(row, column + 1).isOccupied()) {
                    // if there's a letter in a square but nothing next to it
                    int numberEmptySpaces=1;
                    String command = Character.toString(board.getSquare(row, column).getTile().getLetter());
                    while (numberEmptySpaces<=7 && board.getSquare(row, column+numberEmptySpaces).isOccupied()==false) {
                        command = command + "*";
                        numberEmptySpaces++;
                    }
                    command = command + getRowLetterFromNumber(row) + String.valueOf(column) + " A ";
                    emptySpaces.add(command);
                    numberEmptySpaces=1;
                }
            }
        }

        // checking for down positions
        for(int row = 0; row<15; row++) {
            for(int column = 0; column<15; column++) {
                if (board.getSquare(row, column).isOccupied() && !board.getSquare(row+1, column).isOccupied()) {
                    // if there's a letter in a square but nothing next to it
                    int numberEmptySpaces=1;
                    String command = Character.toString(board.getSquare(row, column).getTile().getLetter());                    while (numberEmptySpaces<=7 && board.getSquare(row+numberEmptySpaces, column).isOccupied()==false) {
                        command = command + "*";
                        numberEmptySpaces++;
                    }
                    command = command + getRowLetterFromNumber(row) + String.valueOf(column) + " D ";
                    emptySpaces.add(command);
                    numberEmptySpaces=1;
                }
            }
        }

        return emptySpaces;
    }

    public ArrayList<Word> findPossibleWords(ArrayList<String> emptySpaces) {
        // Variable used to store the solution
        ArrayList<Word> possibleWords = new ArrayList<Word>();

        // Get the frame as a string with just the letters for the permutation
        String s = me.getFrameAsString();
        String frame = "";
        for (int i=1; i<s.length(); i=i+3) {
            frame += s.substring(i, i+1);
        }
        System.out.println("frame = "+frame);


        // Test all combinations possible for each empty spaces
        for (String space : emptySpaces) {
            //System.out.println("C");
            // Get the first letter that's already on the board. Once it's been stored, remove it from the string
            String firstLetter = space.substring(0, 1);
            space = space.substring(1);

            // Get the number of letters to put in the word. Once it's been stored, remove it from the string
            int nbLetters = countMatches(space, '*');
            space = space.replace("*","");

            // Getting all the parameters to convert the string into a Word
            int row = getRowNumberFromLetter(space.substring(0,1));
            int column;
            String acrossOrDown;

            if (space.substring(2,3).equals(" ")) {
                column = Integer.parseInt(space.substring(1, 2));
                acrossOrDown = space.substring(3,4);
            } else {
                column = Integer.parseInt(space.substring(1, 3));
                acrossOrDown = space.substring(4,5);
            }
            boolean isHorizontal = false;
            if (acrossOrDown.equals("A")) {
                isHorizontal = true;
            }


            // Get all the possible permutations with all the letters of the frame
            permutation(firstLetter, frame);

            // Take the right number of letters according to the word's length
            for(String longWord : storeWords) {

                for (int i=nbLetters; i>1; i--) {

                    String newWord = longWord.substring(0, i);
                    Word word = new Word(row, column, isHorizontal, newWord);
                    possibleWords.add(word);
                }
            }
            //checking if the words are valid or not;

            for (int i=0; i<possibleWords.size(); i++)
            {
                if(!dictionary.areWords(possibleWords))
                {
                    possibleWords.remove(i);
                }
                else
                {
                    break;
                }
            }
        }
        return possibleWords;
    }

    public void permutation(String str)
    {
        permutation("", str);
    }

    
    private void permutation(String prefix, String str)
    {
        int n = str.length();
        if (n == 0) storeWords.add(prefix);
        else
        {
            for (int i = 0; i < n; i++)
            {
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
            }
        }
    }

    private String getRowLetterFromNumber(int row)
    {
        return Character.toString((char) (row+64));
    }

    private int getRowNumberFromLetter(String row)
    {
        char c = row.charAt(0);
        return ((int) c)-64;
    }

    private int countMatches(String word, char charToCount)
    {
        int count = 0;

        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == charToCount)
            {
                count++;
            }
        }

        return count;
    }




}
