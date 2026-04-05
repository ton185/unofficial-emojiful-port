package com.hrznstudio.emojiful.util;

import com.hrznstudio.emojiful.Constants;
import com.hrznstudio.emojiful.config.EmojifulConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfanityFilter {

    static final Map<String, String[]> words = new HashMap<>();
    static int largestWordLength = 0;

    public static void loadConfigs() {
        try {
            URL url = new URL("https://docs.google.com/spreadsheets/d/1Ufoero85kpr4caXLLaPpcgwB4tX44GgoGJ4F-bVfdI8/export?format=csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), Charset.forName("UTF-8")));
            String line;
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                counter++;
                try {
                    String[] content = line.split(",");
                    if (content.length == 0) continue;
                    String word = content[0];
                    String[] ignoreWith = content.length > 1 ? content[1].split("_") : new String[]{};
                    if (word.length() > largestWordLength) largestWordLength = word.length();
                    words.put(word.replaceAll(" ", ""), ignoreWith);
                } catch (Exception e) {
                    Constants.LOG.error("Exception loading profanity filter entry", e);
                }
            }
            reader.close();
            Constants.LOG.info("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            Constants.LOG.error("IO Exception in profanity filter", e);
        }
    }

    public static ArrayList<String> badWordsFound(String input) {
        if (input == null) return new ArrayList<>();
        ArrayList<String> badWords = new ArrayList<>();
        for (String word : input.split(" ")) {
            for (int start = 0; start < word.length(); start++) {
                for (int offset = 1; offset < word.length() + 1 - start && offset < largestWordLength; offset++) {
                    String wordToCheck = word.substring(start, start + offset)
                            .replaceAll("1", "i").replaceAll("!", "i")
                            .replaceAll("3", "e").replaceAll("4", "a")
                            .replaceAll("@", "a").replaceAll("5", "s")
                            .replaceAll("7", "t").replaceAll("0", "o")
                            .replaceAll("9", "g")
                            .toLowerCase().replaceAll("[^a-zA-Z]", "");
                    if (words.containsKey(wordToCheck)) {
                        String[] ignoreCheck = words.get(wordToCheck);
                        boolean ignore = false;
                        for (String s : ignoreCheck) {
                            if (input.contains(s)) { ignore = true; break; }
                        }
                        if (!ignore) badWords.add(input.substring(start, start + offset));
                    }
                }
            }
        }
        return badWords;
    }

    public static String filterText(String input) {
        List<String> badWords = badWordsFound(input);
        if (!badWords.isEmpty()) {
            for (String badWord : badWords) {
                input = input.replaceAll(EmojiUtil.cleanStringForRegex(badWord), EmojifulConfig.profanityFilterReplacement);
            }
        }
        return input;
    }
}
