package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for string-related operations.
 */
@UtilityClass
public final class StringUtils {

    /**
     * Generates a random string of the specified length, including letters and optionally numbers.
     *
     * @param length the length of the random string
     * @return a random string of the specified length
     */
    public static String generateRandomString(int length) {
        return generateRandomString(length, true);
    }

    /**
     * Generates a random string of the specified length, including letters and optionally numbers.
     *
     * @param length          the length of the random string
     * @param isUsingNumbers  if true, the generated string will include numbers
     * @return a random string of the specified length
     */
    public static String generateRandomString(int length, boolean isUsingNumbers) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);

        if (isUsingNumbers) {
            characters += numbers;
        }

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }

        return sb.toString();
    }

    /**
     * Removes the last character from a string.
     *
     * @param str the string from which to remove the last character
     * @return the string with the last character removed, or the original string if it is null or empty
     */
    public static String removeLastChar(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }
}
