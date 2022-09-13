package spell;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector{

    private final Trie dictionary = new Trie();
    private final HashSet<String> editDistanceOneSet = new HashSet<String>();
    private final HashSet<String> editDistanceTwoSet = new HashSet<String>();
    private final HashMap<String, Integer> comparisonMap = new HashMap<String, Integer>(); // this is to compare similar words for frequency and alphabet

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        dictionary.resetRoot();

        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            String str = scanner.next();
            str = str.toLowerCase();
            dictionary.add(str);
        }
    }
    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) { return inputWord; } // the word's not misspelled
        else {
            comparisonMap.clear();
            editDistanceOneSet.clear();
            editDistanceTwoSet.clear();

            calculateEditDistance(inputWord, editDistanceOneSet);
            addToComparisonMap(editDistanceOneSet);

            if (comparisonMap.isEmpty()) {
                for(String word : editDistanceOneSet) {
                    calculateEditDistance(word, editDistanceTwoSet);
                    addToComparisonMap(editDistanceTwoSet);
                }
            }

            if (comparisonMap.isEmpty()) return null;

            String suggestion = new String("");
            Integer frequency = 0;

            for (Map.Entry<String, Integer> entry : comparisonMap.entrySet()) {
                if(frequency < entry.getValue()) {
                    frequency = entry.getValue();
                    suggestion = entry.getKey();
                }
                else if(frequency.equals(entry.getValue())) {
                    if(entry.getKey().compareTo(suggestion) < 0) {
                        suggestion = entry.getKey();
                    }
                }
            }

            if(!suggestion.equals("")) return suggestion;
            else return null;


        }
    }
    public void deletionDistance(String inputWord, HashSet<String> setToAddTo) {
        //deletion distance
        for (int i = 0; i < inputWord.length(); i++) {
            StringBuilder sb = new StringBuilder(inputWord);
            sb.deleteCharAt(i);
            String newWord = sb.toString();
            setToAddTo.add(newWord);
        }
    }
    public void transpositionDistance(String inputWord, HashSet<String> setToAddTo) {
        for (int i = 1; i < inputWord.length(); i++) { // has to be one instead of 0 becuase it's n-1 transpositions
            StringBuilder sb = new StringBuilder(inputWord);
            char transpositionChar = sb.charAt(i);
            sb.deleteCharAt(i);
            sb.insert(i - 1, transpositionChar);
            String newWord = sb.toString();
            setToAddTo.add(newWord);
        }
    }
    public void alterationDistance(String inputWord, HashSet<String> setToAddTo) {
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(inputWord);
                sb.deleteCharAt(i);
                char alterationChar = (char) (j + 'a');
                sb.insert(i, alterationChar);
                String newWord = sb.toString();
                setToAddTo.add(newWord);
            }
        }
    }
    public void insertionDistance(String inputWord, HashSet<String> setToAddTo) {
        for (int i = 0; i <= inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(inputWord);
                char alterationChar = (char) (j + 'a');
                sb.insert(i, alterationChar);
                String newWord = sb.toString();
                setToAddTo.add(newWord);
            }
        }
    }
    public void calculateEditDistance(String inputWord, HashSet<String> setToAddTo) {
        deletionDistance(inputWord, setToAddTo);
        transpositionDistance(inputWord, setToAddTo);
        alterationDistance(inputWord, setToAddTo);
        insertionDistance(inputWord, setToAddTo);
    }
    public void addToComparisonMap(HashSet<String> editDistanceSet) {
        for (String word : editDistanceSet) {
            Node foundNode = dictionary.find(word);
            if (foundNode != null) {
                comparisonMap.put(word, foundNode.getValue());
            }
        }
    }

}
