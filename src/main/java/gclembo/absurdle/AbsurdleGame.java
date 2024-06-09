package gclembo.absurdle;

import java.util.*;
import java.io.*;

/**
 * This class is a game of Absurdle.
 */
public class AbsurdleGame {
    private static final String PERFECT_GUESS = "22222";
    private final Set<String> allWords;
    private Set<String> currentWords;
    private int guesses;
    private boolean isGameOver;

    /**
     *  Constructs a new Absurdle game with possible 5-letter words.
     */
    public AbsurdleGame() {
        this.allWords = readWords();
        startNewGame();
    }

    /**
     * @return Number of guesses made in the current game.
     */
    public int getGuesses() {
        return guesses;
    }

    /**
     * @return If the game has been completed
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Resets game to a new game of Absurdle.
     */
    public void startNewGame() {
        this.guesses = 0;
        this.isGameOver = false;
        this.currentWords = new HashSet<>(this.allWords);
    }

    /**
     * Given a guess from the user, considers which guess pattern would be accurate for the
     * greatest number of words in the remaining possible words. All other words are removed
     * from the remaining, words and the pattern corresponding to the remaining words is returned.
     * @param guess Guess from the user.
     * @return Pattern accurate for the greatest number of words from the remaining possible words
     * @throws IllegalArgumentException If the given guess is not 5 characters, is not in the
     * list of all words, or contains non-alphabetical characters.
     */
    public String makeGuess(String guess) {
        if (guess == null || guess.length() != 5) {
            throw new IllegalArgumentException("Guess must be 5 characters long");
        }

        guess = guess.toUpperCase();
        if (!allWords.contains(guess)) {
            throw new IllegalArgumentException("Guess not in list of words");
        }

        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);
            if (!Character.isAlphabetic(letter)) {
                throw new IllegalArgumentException("Use only alphabetical characters");
            }
        }

        // Gets most frequent pattern
        Map<String, Set<String>> patterns = gatherPatterns(guess);
        String bestPattern = getBestPattern(patterns);

        // Sets remaining words to those with the most frequent pattern
        this.currentWords = patterns.get(bestPattern);

        // Checks if user has correct guess
        if (bestPattern.equals(PERFECT_GUESS)) {
            isGameOver = true;
        }

        guesses++;
        return bestPattern;
    }

    /**
     * Given a map of Strings to Sets of Strings, finds the key which is mapped to the largest Set,
     * and returns this key.
     * @param patterns Map of Strings to Sets of Strings to be analyzed.
     * @return Key which is mapped to the largest Set.
     */
    private String getBestPattern(Map<String, Set<String>> patterns) {
        int largestSize = 0;
        String bestPattern = "";

        for (String pattern : patterns.keySet()) {
            if (patterns.get(pattern).size() > largestSize) {
                largestSize = patterns.get(pattern).size();
                bestPattern = pattern;
            }
        }
        return bestPattern;
    }

    /**
     * Reads list of 5-letter words from file "words.txt" and returns
     * the set of all words in the list.
     * @return Set of five-letter words.
     */
    private Set<String> readWords() {
        InputStream input = AbsurdleGame.class.getResourceAsStream("words.txt");
        Scanner fileScan  = new Scanner(new InputStreamReader(input));
        Set<String> words = new HashSet<>();

        while (fileScan.hasNext()) {
            words.add(fileScan.next().toUpperCase());
        }
        return words;
    }

    /**
     * Given a guess, returns a Map of possible patterns to the set of words from the remaining
     * words which would give that pattern.
     * @param guess Possible guess.
     * @return Map of patterns to set of words which the guess would have that pattern.
     */
    private Map<String, Set<String>> gatherPatterns(String guess) {
        Map<String, Set<String>> patterns = new TreeMap<>();
        guess = guess.toUpperCase();

        for (String word : currentWords) {
            String pattern = patternFor(word, guess);

            if (patterns.containsKey(pattern)) {
                patterns.get(pattern).add(word);
            } else {
                Set<String> samePattern = new HashSet<>();
                samePattern.add(word);
                patterns.put(pattern, samePattern);
            }
        }
        return patterns;
    }

    /**
     * Given a word and a guess, returns a guess evaluation pattern
     * for the guess based on the given word.
     * @param word Word to compare guess against.
     * @param guess Guess to compare to word.
     * @return Evaluation pattern for the guess.
     * @throws IllegalArgumentException If the given word and given guess are not the same length.
     */
    public String patternFor(String word, String guess) {
        if (guess.length() != word.length()) {
            throw new IllegalArgumentException("word and guess must be same " +
                    "length to generate pattern");
        }

        // Make case the same
        word = word.toUpperCase();
        guess = guess.toUpperCase();

        char[] pattern = {'0', '0', '0', '0', '0'};
        HashMap<Character, Integer> counts = getCounts(word);

        // Check for exact character matches
        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);

            if (letter == word.charAt(i)) {
                pattern[i] = '2';
                counts.put(letter, counts.get(letter) - 1);
            }
        }

        // Check for non-exact matches, and fills output pattern
        String output = "";
        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);

            if (counts.containsKey(letter)
                    && counts.get(letter) > 0
                    && pattern[i] != '2') {
                output += '1';
                counts.put(letter, counts.get(letter) - 1);
            } else {
                output += pattern[i];
            }
        }
        return output;
    }

    /**
     * Counts number of each character in a given word, and returns a map of each
     * character mapped to the number of times they appear in the word.
     * @param word Word to count characters of
     * @return Map of characters in the given word to the counts of each character in the word
     */
    private HashMap<Character, Integer> getCounts(String word) {
        HashMap<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (counts.containsKey(letter)) {
                counts.put(letter, counts.get(letter) + 1);
            } else {
                counts.put(letter, 1);
            }
        }
        return counts;
    }
}
