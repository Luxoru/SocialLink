package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public final class ArrayUtils {

    public static <T> T[] removeFirstElement(T[] array) {
        if (array == null || array.length == 0) {
            return array;
        }

        return Arrays.copyOfRange(array, 1, array.length);
    }



}
