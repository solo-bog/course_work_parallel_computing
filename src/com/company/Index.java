package com.company;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Index {
    private Map<String, List<WordInfo>> index;
    public Index(){
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
                List<WordInfo> occurrences = index.computeIfAbsent(word,k -> Collections.synchronizedList( new LinkedList<>()));
                occurrences.add(new WordInfo(file.getName(), pos));
            }
        }
    }

    public void serialIndexing(String dataDir){
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

    public void search(List<String> words) {
        try{
            if(index == null) throw new Exception("You must index the files before searching");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        for (String _word : words) {
            String word = _word.toLowerCase();
            List<WordInfo> occurrences = index.get(word);
            result.append(word).append(" ");
            if (occurrences != null) {
                String fileName = "";
                for (WordInfo t : occurrences) {
                    String currentFileName = t.fileName;
                    if(!currentFileName.equals(fileName)){
                        result.append(currentFileName).append(" ");
                        fileName = currentFileName;
                    }
                    result.append(t.position).append(" ");
                }
            }
            else {
                result.append("not found");
            }
            result.append("\n");
        }
        System.out.println(result);
    }

    public void parallelIndexing(String dataDir,int threadsNumber){
        try {
            index = new ConcurrentHashMap<>();
            File[] files = new File(dataDir).listFiles();
            if(files == null) throw new Exception("data directory is not valid");
            System.out.println("\n\n<-----PARALLEL ALGORITHM----->");
            long start = System.currentTimeMillis();
            List<Thread> threads = new ArrayList<>();
            for(int i = 0; i < threadsNumber;i++){
                threads.add(partIndexing(files,i,threadsNumber));
            }
            for(Thread t: threads){
                t.start();
            }
            for(Thread t:threads){
                t.join();
            }
            System.out.println( (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Thread partIndexing(final File[] files, final int partNumber, int threadsNumber) {
        return new Thread(() -> {
            int from = partNumber * (files.length / threadsNumber);
            int to;
            if(partNumber == threadsNumber-1){
                to = files.length;
            }
            else {
                to = (partNumber + 1) * (files.length / threadsNumber);
            }
            for (File file : Arrays.copyOfRange(files, from,to)) {
                try{
                    indexFile(file);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void clear(){
        if(index != null){
            index.clear();
        }
    }

}
