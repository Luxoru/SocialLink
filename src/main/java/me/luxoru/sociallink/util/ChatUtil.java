package me.luxoru.sociallink.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public final class ChatUtil {

    public static String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
