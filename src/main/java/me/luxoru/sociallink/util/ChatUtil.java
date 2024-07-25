package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * Utility class for chat-related operations.
 */
@UtilityClass
public final class ChatUtil {

    /**
     * Formats a string by translating alternate color codes using the '&' character.
     *
     * @param string the string to format
     * @return the formatted string with color codes applied
     */
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
