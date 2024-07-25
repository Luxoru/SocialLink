package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;
import java.util.Arrays;

/**
 * Utility class for array operations.
 */
@UtilityClass
public final class ArrayUtils {

    /**
     * Removes the first element from an array.
     *
     * @param array the array from which to remove the first element
     * @param <T>   the type of the elements in the array
     * @return a new array with the first element removed, or the same array if it is null or empty
     */
    public static <T> T[] removeFirstElement(T[] array) {
        if (array == null || array.length == 0) {
            return array;
        }

        return Arrays.copyOfRange(array, 1, array.length);
    }
}
