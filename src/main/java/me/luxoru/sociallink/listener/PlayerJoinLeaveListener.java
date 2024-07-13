package me.luxoru.sociallink.listener;


import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.user.SocialPlayer;
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

        SocialPlayer socialPlayer = SocialPlayer.createSocialPlayer(player, plugin);

        plugin.getSocialPlayerManager().addPlayer(socialPlayer);

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        SocialPlayer socialPlayer = SocialPlayer.createSocialPlayer(player, plugin);

        plugin.getSocialPlayerManager().removePlayer(socialPlayer);
    }

}
