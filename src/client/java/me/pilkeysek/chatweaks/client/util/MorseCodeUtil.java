package me.pilkeysek.chatweaks.client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MorseCodeUtil {
    private static final char[] english = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            ',', '.', '?', ' '};

    private static final String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
            "-----", "--..--", ".-.-.-", "..--..", " "};

    private static String extractMorseFromString(String s) {
        StringBuilder morseString = new StringBuilder();
        StringBuilder currentMorseChar = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '-':
                case '.':
                case '/':
                    currentMorseChar.append(s.charAt(i));
                    break;
                default:
                    morseString.append(currentMorseChar.toString());
                    morseString.append(' ');
                    currentMorseChar.delete(0, currentMorseChar.length());
                    break;
            }
        }
        if(!currentMorseChar.isEmpty()) {
            morseString.append(currentMorseChar.toString());
        }
        return morseString.toString();
    }
    private static List<String> morseStringToMorseCodeList(String morse) {
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder currentElement = new StringBuilder();
        for(int i = 0; i < morse.length(); i++) {
            switch(morse.charAt(i)) {
                case '-':
                case '.':
                    currentElement.append(morse.charAt(i));
                    break;
                case '/':
                    currentElement.append(' ');
                    break;
                case ' ':
                    if(!currentElement.isEmpty()) {
                        list.add(currentElement.toString());
                        currentElement = new StringBuilder();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected morse code character.");
            }
        }
        if(!currentElement.isEmpty()) {
            list.add(currentElement.toString());
        }
        return list;
    }
    public static String translateStringFromMorse(String s) {
        StringBuilder finalString = new StringBuilder();
        List<String> morseList = morseStringToMorseCodeList(extractMorseFromString(s));
        for(String elem : morseList) {
            for(int i = 0; i < morse.length; i++) {
                if(elem.equals(morse[i])) {
                    finalString.append(english[i]);
                }
            }
        }
        return finalString.toString();
    }
}
