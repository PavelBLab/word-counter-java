package counter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Library implements WordFrequencyAnalyzer {

    // Create a Logger
    static Logger logger = Logger.getLogger(Library.class.getName());

    private Map<String, Integer> wordCounts;
    private List<String> listOfWords;
    private List<Integer> listOfSpaceIndexes;


    public Library() {
        this.wordCounts = new HashMap<>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text1 = scanner.nextLine();
        String text2 = "The sun shines over the lake the THE 111the";

        Library library = new Library();
        logger.log(Level.INFO, String.valueOf(library.calculateHighestFrequency(text1)));
        logger.log(Level.INFO, String.valueOf(library.calculateFrequencyForWord(text1, "lake")));
        logger.log(Level.INFO, String.valueOf(library.calculateMostFrequentNWords(text1, 3)));

        logger.log(Level.INFO, String.valueOf(library.calculateHighestFrequency(text2)));
        logger.log(Level.INFO, String.valueOf(library.calculateFrequencyForWord(text2, "lake")));
        logger.log(Level.INFO, String.valueOf(library.calculateMostFrequentNWords(text2, 3)));

    }

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public void setListOfWords() {
        this.listOfWords = new ArrayList<>();
    }

    public List<Integer> getListOfSpaceIndexes() {
        return listOfSpaceIndexes;
    }

    public void setWordIndexes() {
        this.listOfSpaceIndexes = new ArrayList<>();
    }


    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    public void setWordCounts(String text) {
        setListOfWords();
        setWordIndexes();
        this.wordCounts = new HashMap<>();

        // Cleaning a text from special characters and lowering cases
        String cleanedText = textCleaner(text);

        for (int i = 0; i < cleanedText.length(); i++) {
            if (Character.isWhitespace(cleanedText.charAt(i))) {
                this.listOfSpaceIndexes.add(i);
            }
        }

        this.listOfSpaceIndexes.add(0, 0);
        this.listOfSpaceIndexes.add(this.listOfSpaceIndexes.size(), cleanedText.length());

        for (int i = 0; i < this.listOfSpaceIndexes.size() - 1; i++) {
            this.listOfWords.add(cleanedText.substring(this.listOfSpaceIndexes.get(i), this.listOfSpaceIndexes.get(i + 1)).trim());
        }

        // Alternative solution
        // this.listOfWords.addAll(Arrays.asList(cleanedText.split(" ")));
        // this.library = this.listOfWords.stream().collect(Collectors.toMap(k -> k, v -> 1, Integer::sum));

        for (String word : this.listOfWords) {
            if (this.wordCounts.get(word) == null) {
                this.wordCounts.put(word, 1);
            } else {
                int frequency = this.wordCounts.get(word) + 1;
                this.wordCounts.put(word, frequency);
            }
        }


        // LinkedHashMap preserve the ordering of elements in which they are inserted
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        this.wordCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        this.wordCounts = sortedMap;
    }

    // Text cleaner
    public String textCleaner(String text) {
        String cleanedText = text;
        cleanedText = cleanedText.replaceAll("[^a-zA-Z\\s]", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase();
        return cleanedText;
    }

    @Override
    public int calculateHighestFrequency(String text) {
        setWordCounts(text);

        Integer highestFrequency = 0;
        Optional<Map.Entry<String, Integer>> entry = this.wordCounts.entrySet().stream().findFirst();

        if (entry.isPresent()) {
            highestFrequency = entry.get().getValue();
        }

        return highestFrequency;
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        setWordCounts(text);
        try {
            return this.wordCounts.get(word);
        } catch (NullPointerException e) {
            logger.log(Level.INFO, String.format("%s.%nThere is no such a word. Choose another word", e));
            return 0;
        }
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        setWordCounts(text);

        List<WordFrequency> mostFrequentNWordsList = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : this.wordCounts.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (n < 0) {
                logger.log(Level.INFO, "Enter a number greater then 0");
                return mostFrequentNWordsList;
            }

            if (count == n) {
                break;
            }

            mostFrequentNWordsList.add(new WordFrequencyClass(key, value));
            count++;

        }
        return mostFrequentNWordsList;
    }
}


