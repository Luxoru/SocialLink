package me.luxoru.sociallink.commands;

import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddFriendCommand implements CommandExecutor {

    private final SocialLink plugin;

    public AddFriendCommand(SocialLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player player)){
            return false;
        }
        SocialPlayerManager socialPlayerManager = plugin.getSocialPlayerManager();

        SocialPlayer socialPlayer = socialPlayerManager.getSocialPlayer(player.getUniqueId());

        if(strings.length != 1){
            return false;
        }

        String friend = strings[0];

        socialPlayer.getFriendManager().sendFriendRequest(friend);



        return true;
    }
}
