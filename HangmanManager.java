//Creates class HangmanManager that runs a game of Hangman (with a twist!)
//HangmanManager secretly chooses from a family of words based on user input
//to make the game unnecessarily hard for the user.

import java.util.*;

public class HangmanManager {
    private int guessCount;
    private String currentPattern;
    private Set<Character> guessedLetters;
    private Set<String> wordSet;
    
    //Constructs the base pattern of the word based on the entered int length
    //Creates a directory of words from Collection dictionary that the console considers
    //Creates a guessing limit based on int max
    //Throws IllegalArgumentException if the word length is less than 1
    //or if the guessing limit is negative
    public HangmanManager (Collection<String> dictionary, int length, int max) {
        if (length < 1) {
            throw new IllegalArgumentException("Word length cannot be less than 1");
        } else if (max < 0) {
            throw new IllegalArgumentException("Max incorrect guesses cannot be negative");
        }
        this.guessCount = max;
        this.currentPattern = "-";
        this.guessedLetters = new TreeSet<>();
        for (int i = 1; i < length; i++) {
            currentPattern = currentPattern + " -";
        }
        this.wordSet = new TreeSet<>();
        for (String word : dictionary) {
            if (word.length() == length) {
                wordSet.add(word);
            }
        }
    }
    
    //Returns the current set of words being considered by the console
    public Set<String> words() {
        return wordSet;
    }
    
    //Returns the int amount of guesses left for the user
    public int guessesLeft() {
        return guessCount;
    }
    
    //Returns a set of guessed characters by the user
    public Set<Character> guesses() {
        return guessedLetters;
    }
    
    //Returns the current pattern that the console has set
    //throws IllegalStateException if the set of words is empty 
    public String pattern() {
        if (words().isEmpty()) {
            throw new IllegalStateException("the set of words is empty");
        }
        return currentPattern;
    }
    
    //Takes in a letter guess, case insensitive, stores it,
    //and creates word families for all possible letter patterns. 
    //Chooses the largest family; if the pattern is the same, reduce
    //the amount of guesses by 1 and returns zero, but if it changed,
    //return the amount of times the letter appears.   
    //Throws IllegalStateException if the user has no more guesses left
    //and if the set of words is empty
    //and Throws an IllegalArgumentException if the user has already guessed the letter before
    public int record(char guess) {
        if (guessCount < 1) {
            throw new IllegalStateException("You have no more guesses left!");
        } else if (wordSet.size() == 0) {
            throw new IllegalStateException("the set of words is empty");
        } else if (guessedLetters.contains(guess)) {
            throw new IllegalArgumentException("You already guessed the letter " + guess + "!");
        }
        Map<String, Set<String>> dictionary = new TreeMap<>();
        for (String word : wordSet) {
            String tempPattern = createPattern(word, guess);
            if (!dictionary.containsKey(tempPattern)) {
                Set<String> words = new TreeSet<>();
                dictionary.put(tempPattern, words);
            }
            dictionary.get(tempPattern).add(word);
        }
        return sizeCheck(dictionary, guess);
    }
    
    //Loops through the String word and checks how many times and
    //where char guess (case insensitive) appears in each word
    //creates and returns String pattern
    private String createPattern(String word, char guess) {
        String pattern = currentPattern;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                if (i == word.length() - 1) {
                    pattern = pattern.substring(0, i * 2) + guess;
                } else {
                    pattern = pattern.substring(0, i * 2) + guess + pattern.substring(i * 2 + 1);
                }
            }
        }
        return pattern;
    }
    
    //Compares the sizes of the different word families stored in 
    //Set<String> dictionary and finds the biggest one
    //If the biggest family is the same as the current pattern, 
    //take away one of the guesses and return 0,
    //otherwise returns the total amount of times the char guess
    //appears in the chosen word family
    private int sizeCheck(Map<String, Set<String>> dictionary, char guess) {
        String isDifferent = currentPattern;
        int familySize = 0;
        for (String family : dictionary.keySet()) {
            int newSize = dictionary.get(family).size();
            if (newSize > familySize) {
                familySize = newSize;
                currentPattern = family;
            }
        }
        wordSet = dictionary.get(currentPattern);
        guessedLetters.add(guess);
        if (currentPattern.equals(isDifferent)) {
            guessCount--;
            return 0;       
        } else {
            int letterCount = 0;
            String patternCount = currentPattern;
            for (int i = 0; i < currentPattern.length(); i++) {
               if (currentPattern.charAt(i) == guess) {
                   letterCount++;
               }
            }
            return letterCount;
        }
    }
}
