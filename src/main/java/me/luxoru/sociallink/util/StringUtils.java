package me.luxoru.sociallink.util;

import java.util.concurrent.ThreadLocalRandom;

public class StringUtils {


    public static String generateRandomString(int length) {
        return generateRandomString(length, true);
    }

    public static String generateRandomString(int length, boolean isUsingNumbers){

         String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
         String numbers = "0123456789";

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        StringBuilder sb = new StringBuilder(length);

        if(isUsingNumbers){
            characters += numbers;
        }

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }

        return sb.toString();

    }

    public static String removeLastChar(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

}
