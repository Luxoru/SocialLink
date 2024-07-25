package me.luxoru.sociallink.listener;


import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.data.redis.TimeToLiveRule;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.player.ServerPlayer;
import me.luxoru.sociallink.player.ServerPlayerManager;
import me.luxoru.sociallink.user.SocialPlayer;
import me.luxoru.sociallink.user.SocialPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {

    private final SocialLink plugin;
    private SocialPlayerManager manager;

    public PlayerJoinLeaveListener(SocialLink plugin) {
        this.plugin = plugin;
        this.manager = SocialPlayer.getManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        SocialPlayer socialPlayer = SocialPlayer.getOrCreateSocialPlayer(player, plugin);
        socialPlayer.getServerPlayer().setOnline(true);
        socialPlayer.save();
        manager.addPlayer(socialPlayer, TimeToLiveRule.FOREVER);

        int count = 0;
        for(Message message : socialPlayer.getMessageManager().getMessages()){
            if(message instanceof FriendRequestPendingMessage friendRequestPendingMessage){
                if(friendRequestPendingMessage.isSender(player.getUniqueId()))continue;
                if(friendRequestPendingMessage.hasExpired()){
                    socialPlayer.getMessageManager().removeMessage(message.getMessageUUID());
                    continue;
                }
                count++;
            }
        }

        if(count != 0){
            player.sendMessage(ChatColor.BLUE+"╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸");
            player.sendMessage(ChatColor.GREEN+"You have "+count+" pending friend requests");
            player.sendMessage(ChatColor.YELLOW+"Use "+ChatColor.AQUA+"/f requests "+ChatColor.YELLOW+"to see them!");
            player.sendMessage(ChatColor.BLUE+"╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸╸");


        }


    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        SocialPlayer socialPlayer = manager.getSocialPlayer(player.getUniqueId());
        socialPlayer.getServerPlayer().setOnline(false);
        socialPlayer.save();
        ServerPlayer.getManager().removePlayer(socialPlayer.getServerPlayer());
        manager.removePlayer(socialPlayer);
    }

}
