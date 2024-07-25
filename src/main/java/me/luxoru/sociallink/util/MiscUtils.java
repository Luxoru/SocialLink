package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MiscUtils {

    public static UUID combineUUIDs(UUID uuid1, UUID uuid2) {
        // Extract the most significant and least significant bits from both UUIDs
        long msb1 = uuid1.getMostSignificantBits();
        long lsb1 = uuid1.getLeastSignificantBits();
        long msb2 = uuid2.getMostSignificantBits();
        long lsb2 = uuid2.getLeastSignificantBits();

        // Combine the bits using XOR (or any other combination logic)
        long combinedMsb = msb1 ^ msb2;
        long combinedLsb = lsb1 ^ lsb2;

        // Return a new UUID with the combined bits
        return new UUID(combinedMsb, combinedLsb);
    }

}
