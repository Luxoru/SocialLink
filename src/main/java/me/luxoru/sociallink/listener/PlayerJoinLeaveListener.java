package me.luxoru.sociallink.listener;


import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.message.Message;
import me.luxoru.sociallink.message.friend.FriendRequestPendingMessage;
import me.luxoru.sociallink.user.SocialPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {

    private final SocialLink plugin;

    public PlayerJoinLeaveListener(SocialLink plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SocialPlayer socialPlayer = SocialPlayer.getOrCreateSocialPlayer(player, plugin);
        plugin.getSocialPlayerManager().addPlayer(socialPlayer);
        int count = 0;
        for(Message message : socialPlayer.getMessages()){
            if(message instanceof FriendRequestPendingMessage friendRequestPendingMessage){
                if(friendRequestPendingMessage.hasExpired()){
                    socialPlayer.removeMessage(message);
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

        SocialPlayer socialPlayer = plugin.getSocialPlayerManager().getSocialPlayer(player.getUniqueId());
        plugin.getRedisRepository().insert("socialplayer", socialPlayer);

        plugin.getSocialPlayerManager().removePlayer(player.getUniqueId());


    }

}
