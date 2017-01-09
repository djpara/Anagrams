package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;
    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<Integer, ArrayList> sizeToWords = new HashMap<Integer, ArrayList>();
    HashMap<String, ArrayList> lettersToWord = new HashMap<String, ArrayList>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            // gets all the values at the key: sortedWord
            ArrayList<String> values = lettersToWord.get(sortedWord);
            // check to see if it has values
            if(values == null) {
                // if null, creates a new array list and adds new value
                values = new ArrayList<String>();
                values.add(word);
                lettersToWord.put(sortedWord, values);
            } else {
                if (!values.contains(word)) {
                    // if the arrayList of values does not contain the value and is not null,
                    // adds value to the arrayList
                    values.add(word);
                }
            }
            Integer num = word.length();
            ArrayList<String> intValues = sizeToWords.get(num);
            // check to see if it has values
            if(intValues == null) {
                // if null, creates a new array list and adds new value
                intValues = new ArrayList<String>();
                intValues.add(word);
                sizeToWords.put(num, intValues);
            } else {
                if (!intValues.contains(word)) {
                    // if the arrayList of values does not contain the value and is not null,
                    // adds value to the arrayList
                    intValues.add(word);
                }
            }
        }
    }

       public boolean isGoodWord(String word, String base) {
            // Make sure the word is in our dictionary in the first place
            if (!wordSet.contains(word)) return false;
            for (String w : wordSet) {
                if (word.contains(base)){
                    return false;
                }
            }
            return true;
        }

        public ArrayList<String> getAnagrams(String targetWord) {
            ArrayList<String> result = new ArrayList<String>();
            result = getAnagramsWithOneMoreLetter(targetWord);
            String targetWordSorted = sortLetters(targetWord);
            for (String word : wordList) {
                if (word.length() == targetWordSorted.length()) {
                    String resultSorted = sortLetters(word);
                    if (word.contains(targetWord)) {
                        continue;
                    }
                    if (targetWordSorted.equals(resultSorted)) {
                        result.add(word);
                    }
                }
            }
            return result;
        }

        public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
            ArrayList<String> result = new ArrayList<String>();

            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            String wordToAdd;
            String sortedWordToAdd;

            for (int i = 0; i < alphabet.length(); ++i) {
                wordToAdd = word.concat(alphabet.substring(i,i+1));
                sortedWordToAdd = sortLetters(wordToAdd);
                if (lettersToWord.keySet().contains(sortedWordToAdd)) {
                    ArrayList<String> values = lettersToWord.get(sortLetters(sortedWordToAdd));
                    for (String value : values) {
                        if (value.contains(word)) {
                            continue;
                        } else {
                            result.add(value);
                        }
                    }
                }
            }
            return result;
        }

        public String pickGoodStarterWord() {

            int randIndex = random.nextInt(wordList.size() - 1);
            String word;

            for (;randIndex < wordList.size() - 1; ++randIndex) {
                word = wordList.get(randIndex);
                if (getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS
                        && sizeToWords.get(wordLength).contains(word)) {
                    ++wordLength;
                    return word;
                }
                if (randIndex == wordList.size() - 1) {
                    randIndex = 0;
                    continue;
                }
            }

            return pickGoodStarterWord() ;
        }

        public String sortLetters (String word) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sortedLetters = new String(chars);
            return sortedLetters;
        }
    }
