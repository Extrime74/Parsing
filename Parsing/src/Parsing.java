import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parsing {
    private static Document getPage() throws IOException {
        return Jsoup.connect("https://www.simbirsoft.com").get();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> listOfSeparators = new ArrayList<>();
        listOfSeparators.add(" ");
        listOfSeparators.add(",");
        listOfSeparators.add(".");
        listOfSeparators.add("!");
        listOfSeparators.add("?");
        listOfSeparators.add("-");
        listOfSeparators.add("\"");
        listOfSeparators.add("/");
        listOfSeparators.add(";");
        listOfSeparators.add(":");
        listOfSeparators.add("[");
        listOfSeparators.add("]");
        listOfSeparators.add("(");
        listOfSeparators.add(")");
        listOfSeparators.add("»");
        listOfSeparators.add("«");
        listOfSeparators.add("\n");
        listOfSeparators.add("\r");
        listOfSeparators.add("\t");

        String pretext = getPage().text();
        String separatorsString = String.join("|\\", listOfSeparators);
        Map<String, Word> count = new HashMap<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pretext.getBytes(StandardCharsets.UTF_8))));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(separatorsString);
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                Word wordObj = count.get(word);
                int length = word.length();
                if (length >= 3) {
                    if (wordObj == null) {
                        wordObj = new Word();
                        wordObj.word = word;
                        wordObj.count = 0;
                        count.put(word, wordObj);
                    }
                    wordObj.count++;
                }
            }
        }
        reader.close();

        SortedSet<Word> sortedWords = new TreeSet<>(count.values());
        for (Word word : sortedWords) {
            System.out.println(word.word + "-" + word.count);
        }
    }
    public static class Word implements Comparable<Word> {
        String word;
        int count;

        @Override
        public int hashCode() { return word.hashCode(); }

        @Override
        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }

        @Override
        public int compareTo(Word b) { return b.count - count; }
    }
}

