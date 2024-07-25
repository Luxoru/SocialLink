package me.luxoru.sociallink.commands;

import me.luxoru.sociallink.util.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class SocialLinkCommand implements CommandExecutor {


    private final SocialLinkCommandInfo info;

    private final Map<Set<String>, SocialLinkCommand> subCommands;


    public SocialLinkCommand() {
        if (!getClass().isAnnotationPresent(SocialLinkCommandInfo.class)) { // If the annotation is not present, throw an exception
            throw new IllegalStateException("@SocialLinkSubCommandInfo is not present on " + getClass().getName());
        }
        info = getClass().getAnnotation(SocialLinkCommandInfo.class);
        if(info.name() == null || info.name().isEmpty()) {
            throw new IllegalStateException("@SocialLinkSubCommandInfo name() is not present on " + getClass().getName());
        }
        subCommands = new HashMap<>();

    }

    protected void registerSubCommand(SocialLinkCommand subCommand) {
        subCommands.put(subCommand.getNames(), subCommand);
    }

    protected void unRegisterSubCommand(SocialLinkCommand command) {
        subCommands.remove(command.getNames());
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1){
            return execute(sender, command, label, args);
        }
        String commandName = args[0];

        if(!isValidCommand(commandName)){
            return execute(sender, command, label, args);
        }

        SocialLinkCommand subCommand = getCommand(commandName);
        return subCommand.execute(sender, command, label, ArrayUtils.removeFirstElement(args));

    }


    protected boolean isValidCommand(String commandName){
        for(Set<String> commands : subCommands.keySet()){
            if(commands.contains(commandName)){
                return true;
            }
        }
        return false;
    }

    protected SocialLinkCommand getCommand(String commandName){
        for(Set<String> commands : subCommands.keySet()){
            if(commands.contains(commandName)){
                return subCommands.get(commands);
            }
        }
        return null;
    }


    protected Set<String> getNames(){
        String[] aliases = info.aliases();
        Set<String> set = new HashSet<>(Arrays.asList(aliases));
        set.add(info.name());
        return set;
    }


    public String getName(){
        return info.name();
    }


    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);

}


