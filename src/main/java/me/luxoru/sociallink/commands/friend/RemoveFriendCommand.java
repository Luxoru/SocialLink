package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.commands.friend.redis.FriendRemoveCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.user.SocialPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SocialLinkCommandInfo(name = "remove", aliases = {"rm"}, isSubComand = true)
public class RemoveFriendCommand extends SocialLinkCommand {

    public RemoveFriendCommand() {

        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendRemoveCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendRemoveCommand.class, (command -> {

            Player player = Bukkit.getPlayer(command.getReceiverUUID());

            if(player == null) return;

            player.sendMessage(command.getSenderName()+" has unfriended you!");

            updateSocialPlayer(player);

        }));

    }

    protected void updateSocialPlayer(Player player) {

        SocialPlayer socialPlayer = SocialPlayer.getManager().getSocialPlayer(player.getName());
        if(socialPlayer == null) return;
        socialPlayer.sync();
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        SocialPlayer socialPlayer = SocialPlayer.getManager().getSocialPlayer(player.getUniqueId());

        if(args.length < 1){
            player.sendMessage("Please provide a player to unfriend!");
            return true;
        }

        String friendName = args[0];

        socialPlayer.getFriendManager().removeFriend(friendName);


        return true;

    }
}
