package com.hrznstudio.emojiful;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hrznstudio.emojiful.api.Emoji;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommonClass {

    public static String readStringFromURL(String requestURL) {
        try {
            URL url = new URL(requestURL);
            Scanner scanner = new Scanner(url.openStream(), Charset.forName("UTF-8").name());
            scanner.useDelimiter("\\A");
            String result = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    public static JsonElement readJsonFromUrl(String url) {
        String jsonText = readStringFromURL(url);
        return new JsonParser().parse(jsonText);
    }

    public static List<Emoji> readCategory(String cat) throws YamlException {
        YamlReader categoryReader = new YamlReader(
                new StringReader(readStringFromURL(
                        "https://raw.githubusercontent.com/InnovativeOnlineIndustries/emojiful-assets/1.20-plus/" + cleanURL(cat))));
        return Lists.newArrayList(categoryReader.read(Emoji[].class));
    }

    public static String cleanURL(String string) {
        return string.replaceAll(" ", "%20").replaceAll("&", "%26");
    }

    /**
     * Determines whether a keycode should be ignored for emoji autocomplete.
     * In 1.8.9 we check against Keyboard constants (LWJGL 2).
     */
    public static boolean shouldKeyBeIgnored(int keyCode) {
        // LWJGL 2 key codes: TAB=15, UP=200, DOWN=208, LEFT=203, RIGHT=205
        return keyCode == 15 || keyCode == 200 || keyCode == 208 || keyCode == 203 || keyCode == 205;
    }
}
