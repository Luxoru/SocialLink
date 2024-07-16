package me.luxoru.sociallink.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class SocialLinkCommand implements CommandExecutor {


    protected SocialLinkCommandInfo info;

    private Map<String, SocialLinkCommand> subCommands;


    public SocialLinkCommand() {
        if (!getClass().isAnnotationPresent(SocialLinkCommandInfo.class)) { // If the annotation is not present, throw an exception
            throw new IllegalStateException("@SocialLinkSubCommandInfo is not present on " + getClass().getName());
        }
        info = getClass().getAnnotation(SocialLinkCommandInfo.class);
        subCommands = new HashMap<>();

    }

    protected void registerSubCommand(String commandName, SocialLinkCommand subCommand) {
        subCommands.put(commandName, subCommand);
    }

    protected void unRegisterSubCommand(String commandName) {
        subCommands.remove(commandName);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1){
            return execute(sender, command, label, args);
        }
        String commandName = args[0];
        SocialLinkCommand subCommand = subCommands.get(commandName);
        if(subCommand != null){
            return subCommand.execute(sender, command, label, args);
        }
        return execute(sender, command, label, args);
    }


    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);

}


