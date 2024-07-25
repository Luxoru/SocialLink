package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.commands.friend.redis.FriendAcceptCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import me.luxoru.sociallink.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SocialLinkCommandInfo(name = "accept", isSubComand = true)
public class AcceptFriendCommand extends SocialLinkCommand {

    private SocialPlayerManager manager;

    public AcceptFriendCommand() {
        this.manager = SocialPlayer.getManager();

        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendAcceptCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendAcceptCommand.class, (command -> {

            Player player = Bukkit.getPlayer(command.getPlayerUUID());
            Player friendedPlayer = Bukkit.getPlayer(command.getFriendUUID());
            if(player != null){
                player.sendMessage(ChatUtil.format("&aYou are now friends with "+command.getFriendName()));
                updateSocialPlayer(player);
            }

            if(friendedPlayer != null){
                friendedPlayer.sendMessage(ChatUtil.format("&aYou are now friends with "+command.getPlayerName()));
                updateSocialPlayer(friendedPlayer);
            }


        }));

    }

    protected void updateSocialPlayer(Player player) {

        SocialPlayer socialPlayer = manager.getSocialPlayer(player.getName());
        if(socialPlayer == null) return;
        socialPlayer.sync();
    }




    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return false;

        SocialPlayer socialPlayer = manager.getSocialPlayer(player.getName());
        if(socialPlayer == null){
            //Should never happen
            return false;
        }

        if(args.length != 1){
            player.sendMessage("Please provide a player for you to accept!");
            return true;
        }

        String playerName = args[0];

        ServerPlayer serverPlayer = ServerPlayer.getManager().getPlayer(playerName, true);
        if(serverPlayer == null){
            player.sendMessage("Player has never joined this network before!");
            return false;
        }

        SocialPlayer friendToBe =manager.getSocialPlayer(serverPlayer);

        if(friendToBe == null)return false; //Never happening

        socialPlayer.getFriendManager().acceptFriendRequest(friendToBe);



        return true;



    }
}
