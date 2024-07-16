package me.luxoru.sociallink.commands;

import lombok.Getter;
import me.luxoru.sociallink.SocialLink;
import me.luxoru.sociallink.commands.friend.FriendCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CommandManager {

    private final Map<String, CommandExecutor> commands;

    private final SocialLink plugin;

    public CommandManager(SocialLink plugin) {
        commands = new HashMap<>();
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommand(String commandName, CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command != null) {
            command.setExecutor(commandExecutor);
            commands.put(commandName, commandExecutor);
        } else {
            plugin.getLogger().warning("Failed to register command: " + commandName);
        }
    }

    public void unregisterCommands() {
        for (String commandName : commands.keySet()) {
            PluginCommand command = plugin.getCommand(commandName);
            if (command != null) {
                command.setExecutor(null);
            }
        }
        commands.clear();
    }

    private void registerCommands(){
        registerCommand("friend", new FriendCommand(plugin));
    }


    public Map<String, CommandExecutor> getCommands() {
        return Map.copyOf(commands);
    }
}
