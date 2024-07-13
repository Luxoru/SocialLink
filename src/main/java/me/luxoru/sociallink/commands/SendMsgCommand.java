package me.luxoru.sociallink.commands;

import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMsgCommand implements CommandExecutor {

    private final SocialLink plugin;

    public SendMsgCommand(SocialLink plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        if(!(commandSender instanceof Player player)) {return true;}

        if(strings.length < 1) {return true;}

        SocialPlayer socialPlayer = plugin.getSocialPlayerManager().getSocialPlayer(player.getUniqueId());
        ServerPlayer serverPlayer = socialPlayer.getServerPlayer();
        String friendName = strings[0];



        return true;
    }
}
