package com.company;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Index {
    private final String dataDir;
    private Map<String, List<WordInfo>> index;
    public Index(String dataDir){
        this.dataDir = dataDir;
        this.index = null;
    }
    private final List<String> stopWords = Arrays.asList("a", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "be", "because", "been", "but",
            "by", "can", "cannot", "could", "dear", "did", "do", "does",
            "either", "else", "ever", "every", "for", "from", "get", "got",
            "had", "has", "have", "he", "her", "hers", "him", "his", "how",
            "however", "i", "if", "in", "into", "is", "it", "its", "just",
            "least", "let", "like", "likely", "may", "me", "might", "most",
            "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "rather", "said", "say",
            "says", "she", "should", "since", "so", "some", "than", "that",
            "the", "their", "them", "then", "there", "these", "they", "this",
            "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "will", "with", "would", "yet", "you", "your");

    static private class WordInfo {
        final private String fileName;
        final private int position;

        public WordInfo(String fileName, int position) {
            this.fileName = fileName;
            this.position = position;
        }
    }

    public void indexFile(File file) throws IOException {
        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader
                .readLine()) {
            for (String _word : line.split("\\W+")) {
                String word = _word.toLowerCase();
                pos++;
                if (stopWords.contains(word))
                    continue;
                List<WordInfo> occurrences = index.computeIfAbsent(word,k -> new LinkedList<>());
                occurrences.add(new WordInfo(file.getName(), pos));
            }
        }
    }

    public void serialIndexing(){
        try {
            index = new HashMap<>();
            File[] files = new File(dataDir).listFiles();
            if(files == null) throw new Exception("data directory is not valid");
            System.out.println("\n\n<-----SERIAL ALGORITHM----->");
            long start = System.currentTimeMillis();
            for (File file : files) indexFile(file);
            System.out.println((System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
