package com.hariharanweb.remoteappiummanager.helpers;

/**
 * Created by bsneha on 12/01/18.
 */
public class TryParseInt {
    public static Integer tryParseInt(String someText) {
        try {
            return Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
