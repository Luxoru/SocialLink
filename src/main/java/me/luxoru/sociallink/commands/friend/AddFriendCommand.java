package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SocialLinkCommandInfo(name = "add", isSubComand = true)
public class AddFriendCommand extends SocialLinkCommand {

    private final SocialLink plugin;

    public AddFriendCommand(SocialLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }
        SocialPlayerManager socialPlayerManager = plugin.getSocialPlayerManager();

        SocialPlayer socialPlayer = socialPlayerManager.getSocialPlayer(player.getUniqueId());

        if(args.length != 1){
            return false;
        }

        String friend = args[0];

        boolean b = socialPlayer.getFriendManager().sendFriendRequest(friend);
        if(b){
            player.sendMessage("Sent friend request to " + friend);
        }else{
            player.sendMessage(friend+" has never joined the network :(");
        }


        return true;
    }
}
