package me.pilkeysek.chatweaks.client;

import java.util.List;

public class Util {
    public static boolean startsWithAny(List<String> list, String text) {
        for(String elem : list) {
            if(text.startsWith(elem)) return true;
        }
        return false;
    }
    public static boolean endsWithAny(List<String> list, String text) {
        for(String elem : list) {
            if(text.endsWith(elem)) return true;
        }
        return false;
    }
}
