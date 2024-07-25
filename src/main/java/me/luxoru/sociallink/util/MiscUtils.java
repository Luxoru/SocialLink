package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;
import java.util.UUID;

/**
 * Utility class for miscellaneous operations.
 */
@UtilityClass
public class MiscUtils {

    /**
     * Combines two UUIDs by performing a bitwise XOR operation on their most and least significant bits.
     *
     * @param uuid1 the first UUID to combine
     * @param uuid2 the second UUID to combine
     * @return a new UUID resulting from the combination of the two input UUIDs
     */
    public static UUID combineUUIDs(UUID uuid1, UUID uuid2) {
        // Extract the most significant and least significant bits from both UUIDs
        long msb1 = uuid1.getMostSignificantBits();
        long lsb1 = uuid1.getLeastSignificantBits();
        long msb2 = uuid2.getMostSignificantBits();
        long lsb2 = uuid2.getLeastSignificantBits();

        // Combine the bits using XOR
        long combinedMsb = msb1 ^ msb2;
        long combinedLsb = lsb1 ^ lsb2;

        // Return a new UUID with the combined bits
        return new UUID(combinedMsb, combinedLsb);
    }
}
