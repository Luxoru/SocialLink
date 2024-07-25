package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.commands.friend.redis.FriendMessageCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.friend.FriendManager;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.util.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SocialLinkCommandInfo(name = "msg", aliases = {"say", "tell"})
public class MessageFriendCommand extends SocialLinkCommand{

    public MessageFriendCommand() {

        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendMessageCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendMessageCommand.class, command -> {
            Player player = Bukkit.getPlayer(command.getToUUID());

            if(player == null)return; //Player not in this server

            ServerPlayer serverPlayer = command.getFromPlayer();

            player.sendMessage("["+serverPlayer.getName()+"]: "+command.getMessage());

        });

    }


    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player))return true;

        SocialPlayer socialPlayer = SocialPlayer.getManager().getSocialPlayer(player.getUniqueId());

        if(socialPlayer == null)return true;
        if(args.length == 0){
            player.sendMessage("You need to specify a player to message.");
            return true;
        }

        String friendName = args[0];

        ServerPlayer friendPlayer = ServerPlayer.getManager().getPlayer(friendName);

        if(friendPlayer == null){
            player.sendMessage("That player doesnt exist!");
            return true;
        }

        if(!friendPlayer.isOnline()){
            player.sendMessage(friendPlayer.getName()+" is not online!");
            return true;
        }

        FriendManager friendManager = socialPlayer.getFriendManager();

        if(!friendManager.isFriend(friendPlayer.getUuid())){
            player.sendMessage(friendPlayer.getName()+" is not your friend!");
            return true;
        }

        String message = String.join(" ",ArrayUtils.removeFirstElement(args));

        friendManager.sendMessage(friendPlayer, message);
        player.sendMessage("["+player.getName()+"] "+message);



        return true;
    }
}
