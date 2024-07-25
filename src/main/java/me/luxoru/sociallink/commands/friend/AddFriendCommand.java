package me.luxoru.sociallink.commands.friend;

import me.luxoru.sociallink.commands.SocialLinkCommand;
import me.luxoru.sociallink.commands.SocialLinkCommandInfo;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestCommand;
import me.luxoru.sociallink.commands.friend.redis.FriendRequestExpiredCommand;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import me.luxoru.sociallink.util.ChatUtil;
import me.luxoru.sociallink.util.MiscUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@SocialLinkCommandInfo(name = "add", isSubComand = true)
public class AddFriendCommand extends SocialLinkCommand {

    private final SocialPlayerManager manager;

    public AddFriendCommand() {
        this.manager = SocialPlayer.getManager();
        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendRequestCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendRequestCommand.class, (friendRequest) ->{
            String requesterName = friendRequest.getRequesterName();
            Player recipientPlayer = Bukkit.getPlayer(friendRequest.getReceiverId());
            System.out.println(recipientPlayer);
            if(recipientPlayer == null) {
                //Not in this server
                return;
            }


            recipientPlayer.sendMessage(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));

            recipientPlayer.sendMessage(ChatUtil.format("&eFriend request from &a"+requesterName));

            TextComponent accept = new TextComponent(ChatUtil.format("&l&a[ACCEPT]"));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Add friend")));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "fr accept "+requesterName));

            TextComponent reject = new TextComponent(ChatUtil.format("&l&c[REJECT]"));
            reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Reject friend")));
            reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "fr reject "+requesterName));

            recipientPlayer.spigot().sendMessage(accept, TextComponent.fromLegacy(" - "), reject);
            recipientPlayer.sendMessage(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));

            ServerPlayer serverPlayer = ServerPlayer.getManager().getPlayer(friendRequest.getReceiverName());
            SocialPlayer socialPlayer = manager.getSocialPlayer(serverPlayer);
            socialPlayer.getMessageManager().removeMessage(MiscUtils.combineUUIDs(friendRequest.getRequesterId(),friendRequest.getReceiverId()));



        });
        RedisDatabaseAdapter.INSTANCE.getManager().addCommand(FriendRequestExpiredCommand.class);
        RedisDatabaseAdapter.INSTANCE.getManager().addCallback(FriendRequestExpiredCommand.class, (command) ->{
            UUID commandTo = command.getTo();
            Player player = Bukkit.getPlayer(commandTo);

            if(player == null) return; //Player not in this server

            player.sendMessage(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));
            if(command.isSender()){
                player.sendMessage(ChatUtil.format("&eYour friend request to &a"+command.getFromName()+"&e has expired."));
            }
            else{
                player.sendMessage(ChatUtil.format("&eThe request from &a"+command.getFromName()+"&e has expired."));
            }

            player.sendMessage(ChatUtil.format("&9╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸"));


        });
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }


        SocialPlayer socialPlayer = manager.getSocialPlayer(player.getUniqueId());



        if(args.length != 1){

            player.sendMessage("Provide a player to friend :)");

            return false;
        }

        String friend = args[0];

        if(friend.equalsIgnoreCase(player.getName())){
            player.sendMessage("Can't friend yourself :(");
            return true;
        }

        socialPlayer.getFriendManager().sendFriendRequest(friend);


        return true;
    }
}
