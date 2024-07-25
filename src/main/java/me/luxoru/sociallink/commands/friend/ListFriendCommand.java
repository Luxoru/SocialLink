package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SocialLinkCommandInfo(name = "list", aliases = {"l"}, isSubComand = true)
public class ListFriendCommand extends SocialLinkCommand {

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player))return true;

        SocialPlayer socialPlayer = SocialPlayer.getManager().getSocialPlayer(player.getUniqueId());

        Set<UUID> friends = socialPlayer.getFriendManager().getFriends();

        Set<ServerPlayer> serverPlayers = ServerPlayer.getManager().getPlayersFromIds(friends);

        List<String> messages = new ArrayList<>();

        for(ServerPlayer serverPlayer : serverPlayers) {
            messages.add("Your friend "+serverPlayer.getName()+" is "+(serverPlayer.isOnline()?"online in the server "+serverPlayer.getServerName() :"offline"));
        }

        if(!messages.isEmpty()) {
            messages.addFirst(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));
            messages.addLast(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));
        }

        player.sendMessage(String.valueOf(messages));


        return true;

    }
}

